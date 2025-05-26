package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import com.nichi.nikkie225.model.Tradedto;
import com.nichi.nikkie225.model.dao.Pricedto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.nichi.nikkie225.model.Database.getConnection;

public class TradeService {
    List<TradeEntry> master=new ArrayList<>();
    List<Pricedto> master1 = new ArrayList<>();
    public List<TradeEntry> getDataTrade(){
        try(Connection conn= getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from tradeslist")){
            ResultSet rs= stm.executeQuery();
            while (rs.next()){
                TradeEntry trade= new TradeEntry(
                        rs.getInt("tradeno"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("tradedate"),
                        rs.getString("side"),
                        rs.getInt("tradeprice"),
                        rs.getInt("quantity")
                );
                master.add(trade);
            }
            return master;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Pricedto> price(){
        try (Connection conn = getConnection();
        PreparedStatement stm = conn.prepareStatement("select * from stockspricelist")
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                Pricedto price = new Pricedto(
               rs.getString("dt"),
                rs.getString("code"),
                rs.getString("market"),
                rs.getInt("price")
                );
                master1.add(price);
            }
            return master1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
