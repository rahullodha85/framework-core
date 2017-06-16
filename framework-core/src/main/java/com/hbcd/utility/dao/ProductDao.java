package com.hbcd.utility.dao;
/**
 * Created by williskong on 7/22/2015.
 */
import java.util.ArrayList;

public interface ProductDao {
    ArrayList<String> getProducts(String retData, Object criteria) throws Exception;
}
