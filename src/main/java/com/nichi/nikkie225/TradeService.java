package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import com.nichi.nikkie225.model.Tradedto;
import com.nichi.nikkie225.model.dao.Pricedto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

import static com.nichi.nikkie225.model.Database.getConnection;

public class TradeService {


    public List<TradeEntry> getDataTradeUntil(Date date) {
        List<TradeEntry> master = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(
                     "SELECT * FROM tradeslist WHERE tradedate <= ? ORDER BY tradedate, tradeno")) {
            stm.setDate(1, date);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                TradeEntry trade = new TradeEntry(
                        rs.getInt("tradeno"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("tradedate"),
                        rs.getString("side"),

                        rs.getDouble("tradeprice"),
                        rs.getDouble("quantity"),
                        rs.getString("isdeleted")
                        );
                master.add(trade);
            }
            return master;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Pricedto> price() {
        List<Pricedto> master1 = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement("select * from stockspricelist")) {

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Pricedto price = new Pricedto(
                        rs.getString("dt"),
                        rs.getString("code"),
                        rs.getString("market"),
                        rs.getDouble("price")
                );
                master1.add(price);
            }
            return master1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Integer> computePositionUntil(Date date) {
        List<TradeEntry> trades = getDataTradeUntil(date);
        Map<String, Integer> positionMap = new HashMap<>();

        for (TradeEntry entry : trades) {
            String key = entry.getCode() + "|" + entry.getName();
            Double quantity = entry.getQuantity();
            int currentPosition = positionMap.getOrDefault(key, 0);

            if ("B".equalsIgnoreCase(entry.getSide())) {
                currentPosition += quantity;
            } else if ("S".equalsIgnoreCase(entry.getSide())) {
                currentPosition -= quantity;
            }
            positionMap.put(key, currentPosition);
        }
        return positionMap;
    }

    public List<Tradedto> computePositionsForDate(LocalDate selectedDate) {
        Date sqlDateT = Date.valueOf(selectedDate);
        Date sqlDateTMinus1 = Date.valueOf(selectedDate.minusDays(1));

        Map<String, Integer> positionT = computePositionUntil(sqlDateT);
        Map<String, Integer> positionTMinus1 = computePositionUntil(sqlDateTMinus1);

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(positionT.keySet());
        allKeys.addAll(positionTMinus1.keySet());

        List<Tradedto> results = new ArrayList<>();
        for (String key : allKeys) {
            String[] parts = key.split("\\|");
            String code = parts[0];
            String name = parts[1];

            double posT = positionT.getOrDefault(key, 0);
            double posTMinus1 = positionTMinus1.getOrDefault(key, 0);

            Tradedto dto = new Tradedto();
            dto.setStocks(code);
            dto.setName(name);
            dto.setPositionT(posT);
            dto.setPositionTm1(posTMinus1);
            dto.setTradePrice(0.0);

            results.add(dto);
        }
        return results;
    }


    public int computePositionForCodeOnDate(String code, LocalDate previousDate) {
        return 0;
    }
}
