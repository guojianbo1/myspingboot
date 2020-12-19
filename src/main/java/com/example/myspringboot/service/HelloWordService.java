package com.example.myspringboot.service;

import com.example.myspringboot.mode.HelloWord;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * service
 *
 * @author guojianbo
 * @date 2020-11-10 13:37
 */
public class HelloWordService {

    public HelloWord sayHelloWord(){
        HelloWord helloWord = new HelloWord();
        helloWord.msg="hello word222";
        return helloWord;
    }

    public String aaa(){
        return "aaaa";
    }

    public String bbb(){
        return "bbb";
    }

    public String ccc(){
        return "ccc";
    }

    public String ddd(){
        return "ddd";
    }

    public String eee(){
        return "eee";
    }


    public static void main(String[] args) {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        Map<String,Map<String,Object>> resultMap = null;
        String cellData = null;
        String filePath = "C:\\Users\\admin\\Desktop\\月亮券支付优惠信息导入表20201219.xlsx";
        String columns[] = {"*产品编码（SAP）","*使用月亮券支付优惠的金额"};
        wb = readExcel(filePath);
        if(wb != null){
            //用来存放表中数据
            resultMap = new LinkedHashMap<String,Map<String,Object>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            for (int i = 1; i<rownum; i++) {
                row = sheet.getRow(i);
                if(row !=null){
                    String sku =(String) getCellFormatValue(row.getCell(2));
                    BigDecimal discountB = new BigDecimal( (String) getCellFormatValue(row.getCell(9))) ;
                    discountB = discountB.multiply(new BigDecimal(100));
                    Integer discount = discountB.intValue();
                    if (sku!=null && discount!=null) {
                        Map<String, Object> map = new LinkedHashMap<String, Object>();
                        map.put("sku", sku);
                        map.put("moon_pay_discount", discount);
                        resultMap.put(sku,map);
                    }
                }else{
                    break;
                }
            }
        }
        //遍历解析出来的list
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuilder sql = new StringBuilder();
        sql.append("insert into mall_item_moonpay_discount_rule (sku,moon_pay_discount,create_time) values ");
        StringBuilder osql = new StringBuilder();
        osql.append("select * from mall_item_moonpay_discount_rule where sku in (");
        for (Map.Entry<String,Map<String,Object>> entry : resultMap.entrySet()) {
            Map<String, Object> map = entry.getValue();
            sql.append("('").append(map.get("sku")).append("',").append(map.get("moon_pay_discount")).append(",'").append(date).append("')").append(",");
            osql.append("'").append(map.get("sku")).append("',");
        }
        String resultSql = sql.substring(0, sql.length() - 1);
        String resultOSql = osql.substring(0, osql.length() - 1)+");";
        System.out.println(resultOSql);
        System.out.println(resultSql);
    }
    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        cell.setCellType(CellType.STRING);
        Object cellValue = cell.getStringCellValue();
        return cellValue;
    }
}
