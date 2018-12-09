package com.nis.util.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

  
/** 
 * 使用CVS模式解决XLSX文件，可以有效解决用户模式内存溢出的问题 
 * 该模式是POI官方推荐的读取大数据的模式，在用户模式下，数据量较大、Sheet较多、或者是有很多无用的空行的情况 
 * ，容易出现内存溢出,用户模式读取Excel的典型代码如下： FileInputStream file=new 
 * FileInputStream("c:\\test.xlsx"); Workbook wb=new XSSFWorkbook(file); 
 *  
 *  
 * @author 山人 
 */  
public abstract class XLSXCovertCSVReader {  
  
    /** 
     * The type of the data value is indicated by an attribute on the cell. The 
     * value is usually in a "v" element within the cell. 
     */  
    enum xssfDataType {  
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,  
    }  
    /** 
     * 使用xssf_sax_API处理Excel,请参考： http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api 
     * <p/> 
     * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at 
     * http://www.ecma-international.org/publications/standards/Ecma-376.htm 
     * <p/> 
     * A web-friendly version is http://openiso.org/Ecma/376/Part4 
     */  
    //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等  
    private String preRefnum = null, refnum = null;  
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格  
    private String maxRefnum = null; 
    private String lastContents;
    private int curCol = 0;  
//    private Integer type;
    private List<IndexValue> rowData= new ArrayList<IndexValue>();
    //excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型  
    public abstract List<Object> optRows(int sheetIndex,int curRow, List<Object> rowlist) ;  
    class MyXSSFSheetHandler extends DefaultHandler {  
  
        /** 
         * Table with styles 
         */  
        private StylesTable stylesTable;  
  
        /** 
         * Table with unique strings 
         */  
        private ReadOnlySharedStringsTable sharedStringsTable;  
  
  
        /** 
         * Number of columns to read starting with leftmost 
         */  
  
        // Set when V start element is seen  
        private boolean vIsOpen;  
  
        // Set when cell start element is seen;  
        // used when cell close element is seen.  
        private xssfDataType nextDataType;  
        private int sheetIndex = -1;  
        // Used to format numeric cell values.  
        private short formatIndex;  
        private String formatString;  
        private final DataFormatter formatter;  
  
        private int thisColumn = -1;  
        // The last column printed to the output stream  
        private int lastColumnNumber = -1;  
        private List<Object> rowlist = new ArrayList<Object>();  
        // Gathers characters as they are seen.  
        private StringBuffer value;  
//        private String[] record;  
      //  private List<String[]> rows = new ArrayList<String[]>();  
        private boolean isCellNull = false;  
  
        /** 
         * Accepts objects needed while parsing. 
         *  
         * @param styles 
         *            Table of styles 
         * @param strings 
         *            Table of shared strings 
         * @param cols 
         *            Minimum number of columns to show 
         * @param target 
         *            Sink for output 
         */  
        public MyXSSFSheetHandler(StylesTable styles,  
                ReadOnlySharedStringsTable strings) {  
            this.stylesTable = styles;  
            this.sharedStringsTable = strings;  
            this.value = new StringBuffer();  
            this.nextDataType = xssfDataType.NUMBER;  
            this.formatter = new DataFormatter();  
            rowlist.clear();// 每次读取都清空行集合  
        }  
  
        /* 
         * (non-Javadoc) 
         *  
         * @see 
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, 
         * java.lang.String, java.lang.String, org.xml.sax.Attributes) 
         */  
        public void startElement(String uri, String localName, String name,  
                Attributes attributes) throws SAXException {  
  
            if ("inlineStr".equals(name) || "v".equals(name)) {  
                vIsOpen = true;  
                // Clear contents cache  
                value.setLength(0);  
            }  
            // c => cell  
            else if ("c".equals(name)) {  
                // Get the cell reference  
                String r = attributes.getValue("r");  
                int firstDigit = -1;  
                for (int c = 0; c < r.length(); ++c) {  
                    if (Character.isDigit(r.charAt(c))) {  
                        firstDigit = c;  
                        break;  
                    }  
                }  
                thisColumn = nameToColumn(r.substring(0, firstDigit));  
                String cellType = attributes.getValue("t");  
                
                if(preRefnum == null){  
                    preRefnum = attributes.getValue("r");  
                }
//                else{  
//                    preRefnum = refnum;  
//                }  
                //当前单元格的位置  
                refnum = attributes.getValue("r");  
                  
                  
                // Figure out if the value is an index in the SST  
                  
                // Set up defaults.  
                this.nextDataType = xssfDataType.NUMBER;  
                this.formatIndex = -1;  
                this.formatString = null;  
              
                String cellStyleStr = attributes.getValue("s");  
                if ("b".equals(cellType))  
                    nextDataType = xssfDataType.BOOL;  
                else if ("e".equals(cellType))  
                    nextDataType = xssfDataType.ERROR;  
                else if ("inlineStr".equals(cellType))  
                    nextDataType = xssfDataType.INLINESTR;  
                else if ("s".equals(cellType))  
                    nextDataType = xssfDataType.SSTINDEX;  
                else if ("str".equals(cellType))  
                    nextDataType = xssfDataType.FORMULA;  
                else if (cellStyleStr != null) {  
                    // It's a number, but almost certainly one  
                    // with a special style or format  
                    int styleIndex = Integer.parseInt(cellStyleStr);  
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);  
                    this.formatIndex = style.getDataFormat();  
                    this.formatString = style.getDataFormatString();  
                    if (this.formatString == null)  
                        this.formatString = BuiltinFormats  
                                .getBuiltinFormat(this.formatIndex);  
                }  
            }  
            lastContents = "";  
        }  
  
        /* 
         * (non-Javadoc) 
         *  
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, 
         * java.lang.String, java.lang.String) 
         */  
        public void endElement(String uri, String localName, String name)  
                throws SAXException {  
  
            Object thisVal = null;  
  
            // v => contents of a cell  
            if ("v".equals(name)) {  
                // Process the value contents as required.  
                // Do now, as characters() may be called more than once  
                switch (nextDataType) {  
  
                case BOOL:  
                    char first = value.charAt(0);  
                    thisVal = first == '0' ? false : true;  
                    break;  
  
                case ERROR:  
                	thisVal = "\"ERROR:" + value.toString() + '"';  
                    break;  
  
                case FORMULA:  
                    // A formula could result in a string value,  
                    // so always add double-quote characters.  
                	thisVal =  value.toString() ;  
                    break;  
  
                case INLINESTR:  
                    // TODO: have seen an example of this, so it's untested.  
                    XSSFRichTextString rtsi = new XSSFRichTextString(  
                            value.toString());  
                    thisVal = rtsi.toString() ;  
                    break;  
  
                case SSTINDEX:  
                    String sstIndex = value.toString();  
                    try {  
                        int idx = Integer.parseInt(sstIndex); 
                        XSSFRichTextString rtss = new XSSFRichTextString(  
                                sharedStringsTable.getEntryAt(idx));  
                        thisVal =   rtss.toString();  
                    } catch (NumberFormatException ex) {  
                        System.out.println("Failed to parse SST index '" + sstIndex  
                                + "': " + ex.toString());  
                    }  
                    break;  
  
                case NUMBER:  
                	String n = value.toString();  
                    // 判断是否是日期格式  
                    if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {  
                        Double d = Double.parseDouble(n);  
                        Date date=HSSFDateUtil.getJavaDate(d);  
                        thisVal=date;  
                    } else if (this.formatString != null)  
                    	thisVal = formatter.formatRawCellContents(  
                                Double.parseDouble(n), this.formatIndex,  
                                this.formatString);  
                    else if(n.indexOf("E")!=-1) {//处理科学计数法
                    	BigDecimal bd = new BigDecimal(n);
                    	thisVal = bd.toPlainString();  
                    }else 
                    	thisVal = n;  
                    break;  
  
                default:  
                	thisVal = "(TODO: Unexpected type: " + nextDataType + ")";  
                    break;  
                }  
  
                // Output after we've seen the string contents  
                // Emit commas for any fields that were missing on this row  
                if (lastColumnNumber == -1) {  
                    lastColumnNumber = 0;  
                }  
                
                if(!refnum.equals(preRefnum)){  
                    int len = countNullCell(refnum, preRefnum);  
                    for(int i=0;i<len;i++){  
                        rowlist.add(curCol, "");  
                        curCol++;  
                    }  
                }  
              //判断单元格的值是否为空  
                if (thisVal == null || "".equals(isCellNull)) {  
                  //  isCellNull = true;// 设置单元格是否为空值  
                }else{
                	preRefnum = refnum; 
                }
//                System.out.println("refnum="+refnum+"preRefnum="+preRefnum+"curCol="+curCol);
                rowlist.add(curCol, thisVal); 
                rowData.add(new IndexValue(refnum,lastContents) );
                curCol++;   
              //  record[thisColumn] = thisStr;  
                // Update column  
                if (thisColumn > -1)  
                    lastColumnNumber = thisColumn;  
  
            } else if ("row".equals(name)) {  
  
                // Print out any missing commas if needed  
                if (thisColumn > 0) {  
                	sheetIndex++;
                    // Columns are 0 based  
                    if (lastColumnNumber == -1) {  
                        lastColumnNumber = 0;  
                    }  
                    //默认第一行为表头，以该行单元格数目为最大数目  
                    if(sheetIndex == 0){  
                        maxRefnum = refnum;  
                    }  
                    //补全一行尾部可能缺失的单元格  
                    if(maxRefnum != null){  
                        int len = countNullCell(maxRefnum, refnum);  
                        for(int i=0;i<=len;i++){  
                            rowlist.add(curCol, "");  
                            curCol++;  
                        }  
                    }  
                    if ( rowlist.get(0) != null  
                            && rowlist.get(1) != null)// 判断是否空行  
                    {  
                    	optRows(sheetIndex,lastColumnNumber,rowlist);
                    	
                    	rowlist.clear();
//                        rows.add(record.clone());  
                        isCellNull = false;  
//                        for (int i = 0; i < record.length; i++) {  
//                            record[i] = null;  
//                        }  
                    }  
                }  
                rowlist.clear();
                rowData.clear();
                curCol = 0;   
                preRefnum = null;  
                refnum = null;  
                lastColumnNumber = -1;  
            }  
  
        }  
  
//        public List<String[]> getRows() {  
//            return rows;  
//        }  
//  
//        public void setRows(List<String[]> rows) {  
//            this.rows = rows;  
//        }  
  
        /** 
         * Captures characters only if a suitable element is open. Originally 
         * was just "v"; extended for inlineStr also. 
         */  
        public void characters(char[] ch, int start, int length)  
                throws SAXException {  
            if (vIsOpen)  
                value.append(ch, start, length);  
        }  
  
        /** 
         * Converts an Excel column name like "C" to a zero-based index. 
         *  
         * @param name 
         * @return Index corresponding to the specified name 
         */  
        private int nameToColumn(String name) {  
            int column = -1;  
            for (int i = 0; i < name.length(); ++i) {  
                int c = name.charAt(i);  
                column = (column + 1) * 26 + c - 'A';  
            }  
            return column;  
        }  
  
        private String formateDateToString(Date date) {  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化日期  
            return sdf.format(date);  
  
        }  
  
    }  
  
    // /////////////////////////////////////  
  
    private String path;  
  
    /** 
     * Creates a new XLSX -> CSV converter 
     *  
     * @param pkg 
     *            The XLSX package to process 
     * @param output 
     *            The PrintStream to output the CSV to 
     * @param minColumns 
     *            The minimum number of columns to output, or -1 for no minimum 
     */  
    public XLSXCovertCSVReader(  
            String path) {  
        this.path = path;  
    }  
    public XLSXCovertCSVReader(  
           ) {  
    }  
    /** 
     * Parses and shows the content of one sheet using the specified styles and 
     * shared-strings tables. 
     *  
     * @param styles 
     * @param strings 
     * @param sheetInputStream 
     */  
    public void processSheet(StylesTable styles,  
            ReadOnlySharedStringsTable strings, InputStream sheetInputStream)  
            throws IOException, ParserConfigurationException, SAXException {  
  
        InputSource sheetSource = new InputSource(sheetInputStream);  
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();  
        SAXParser saxParser = saxFactory.newSAXParser(); 
        XMLReader sheetParser = saxParser.getXMLReader();  
        MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings);  
        sheetParser.setContentHandler(handler);  
        sheetParser.parse(sheetSource);  
    }  
  
    /** 
     * 初始化这个处理程序 将 
     *  
     * @throws IOException 
     * @throws OpenXML4JException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */  
    public void processOneSheet(String path,int sheetId) throws IOException, OpenXML4JException,  
            ParserConfigurationException, SAXException,SQLException {  
    	 OPCPackage xlsxPackage = OPCPackage.open(path, PackageAccess.READ); 
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(  
                xlsxPackage);  
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
        List<String[]> list = null;  
        StylesTable styles = xssfReader.getStylesTable();  
        InputStream stream=xssfReader.getSheet("rId"+sheetId);
        processSheet(styles, strings,stream );  
        stream.close(); 
        xlsxPackage.close();  
    }  
    public void processOneSheet(File file,int sheetId) throws IOException, OpenXML4JException,  
    ParserConfigurationException, SAXException,SQLException {  
    	OPCPackage xlsxPackage = OPCPackage.open(file, PackageAccess.READ); 
    	ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(  
    			xlsxPackage);  
    	XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
    	List<String[]> list = null;  
    	StylesTable styles = xssfReader.getStylesTable();  
    	InputStream stream=xssfReader.getSheet("rId"+sheetId);
    	processSheet(styles, strings,stream );  
    	stream.close(); 
    	xlsxPackage.close();  
    }  
    /** 
     * 读取Excel 
     *  
     * @param path 
     *            文件路径 
     * @param sheetName 
     *            sheet名称 
     * @param minColumns 
     *            列总数 
     * @return 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws OpenXML4JException 
     * @throws IOException 
     */  
    public static void readerExcel(String path, String sheetName,  
            int minColumns) throws IOException, OpenXML4JException,  
            ParserConfigurationException, SAXException {  
        OPCPackage p = OPCPackage.open(path, PackageAccess.READ);  
//        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, 
//                sheetName);  
        // xlsx2csv.process(p, sheetName);  
        p.close();  
    }  
  
public List<String> getMyDataList(List<IndexValue> dataList) {  
        
        List<String> myDataList = new ArrayList<String>();  
        if(dataList==null||dataList.size()<=0) return myDataList;  
          
        for(int i=0;i<dataList.size()-1;i++){  
            IndexValue current  = dataList.get(i);  
            myDataList.add(current .v_value); 
            IndexValue next = dataList.get(i+1);
            int level = next.getLevel(current); 
            for(int k = 0;k<level-1;k++){  
            	myDataList.add(null);  
            } 
              if(i==dataList.size()-2){
            	  myDataList.add(next .v_value); 
              }
              
        }  
        return myDataList;  
    } 
    private class IndexValue{  
        String v_index;  
        String v_value;  
        public IndexValue(String v_index, String v_value) {  
            super();  
            this.v_index = v_index;  
            this.v_value = v_value;  
        }  
        @Override  
        public String toString() {  
            return "IndexValue [v_index=" + v_index + ", v_value="  
                    + v_value + "]";  
        }  
        public int getLevel(IndexValue p){  
          
            char[] other = p.v_index.replaceAll("[0-9]", "").toCharArray();  
            char[] self = this.v_index.replaceAll("[0-9]", "").toCharArray();  
            if(other.length!=self.length) return -1;  
            for(int i=0;i<other.length;i++){  
                if(i==other.length-1){  
                    return self[i]-other[i];  
                }else{  
                    if(self[i]!=other[i]){  
                        return -1;  
                    }  
                }  
                  
            }  
            return -1;  
              
        }  
    } 
    /** 
     * 计算两个单元格之间的单元格数目(同一行) 
     * @param ref 
     * @param preRef 
     * @return 
     */  
    public int countNullCell(String ref, String preRef){  
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD  
        String xfd = ref.replaceAll("\\d+", "");  
        String xfd_1 = preRef.replaceAll("\\d+", "");  
          
        xfd = fillChar(xfd, 3, '@', true);  
        xfd_1 = fillChar(xfd_1, 3, '@', true);  
          
        char[] letter = xfd.toCharArray();  
        char[] letter_1 = xfd_1.toCharArray();  
        int res = (letter[0]-letter_1[0])*26*26 + (letter[1]-letter_1[1])*26 + (letter[2]-letter_1[2]);  
        return res-1;  
    }
    /** 
     * 字符串的填充 
     * @param str 
     * @param len 
     * @param let 
     * @param isPre 
     * @return 
     */  
    String fillChar(String str, int len, char let, boolean isPre){  
        int len_1 = str.length();  
        if(len_1 <len){  
            if(isPre){  
                for(int i=0;i<(len-len_1);i++){  
                    str = let+str;  
                }  
            }else{  
                for(int i=0;i<(len-len_1);i++){  
                    str = str+let;  
                }  
            }  
        }  
        return str;  
    }  
    public void characters(char[] ch, int start, int length)  
            throws SAXException {  
        //得到单元格内容的值  
        lastContents += new String(ch, start, length);  
    }  
}  
