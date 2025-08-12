package com.nichi.nikkie225;

import com.nichi.nikkie225.dto.ComboDataDTO;
import com.nichi.nikkie225.dto.MinMaxBound;
import com.nichi.nikkie225.model.dao.TradeEntryHelper;
import com.nichi.nikkie225.model1.TradeList;
import com.nichi.nikkie225.model1.TradeListId;


import com.nichi.nikkie225.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TradeEntryDAO {
    public void saveAll(List<TradeList> tradeLists) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = (Transaction) session.beginTransaction();

            for (TradeList trade : tradeLists) {
                session.merge(trade);
            }

            transaction.commit();

        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());

        }finally {
            session.close();

        }
    }

    public List<TradeList> getAllTradeList() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        List<TradeList> tradeLists = null;

        try {
            Query<TradeList> query = session.createQuery("FROM TradeList where isDeleted=0 order by tradeNo asc", TradeList.class);
            tradeLists = query.list();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return tradeLists;
    }

    public List<TradeList> getAllTrades() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        List<TradeList> tradeLists = null;

        try {
            Query<TradeList> query = session.createQuery("FROM TradeList where order by tradeNo asc", TradeList.class);
            tradeLists = query.list();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return tradeLists;
    }

    public void deleteTrade(Integer tradeNo, String code) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = (Transaction) session.beginTransaction();
            TradeListId tradeListId = new TradeListId(tradeNo, code);
            TradeList tradeList = session.get(TradeList.class, tradeListId);

            if (tradeList != null) {
                session.remove(tradeList);
            }
            transaction.commit();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
    }

    public void deleteTrade(List<TradeEntryHelper> deletedValues) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;

        try {


            for (var data : deletedValues) {
                transaction = (Transaction) session.beginTransaction();
                TradeListId tradeListId = new TradeListId(data.getTradeNo(), data.getCode());
                TradeList trade = session.get(TradeList.class, tradeListId);


                if (trade != null) {
                    trade.setIsDeleted(1);
                    System.out.println("delete aythu");
                }
                transaction.commit();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
    }


    public List<ComboDataDTO> getCodeData() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        String selectDistinctCode = "SELECT DISTINCT code FROM tradeslist";
        List<ComboDataDTO> combo = new ArrayList<>();
        try {
            List<String> codes = session.createNativeQuery(selectDistinctCode).getResultList();
            for (String v : codes) {
                combo.add(new ComboDataDTO(v));
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return combo;
    }

    public List<MinMaxBound> getMinMaxValues(String date) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        String selectMinMixValue =
                "SELECT code, lowerband, upperband FROM stockspricelist WHERE dt = :date";

        List<MinMaxBound> boundsList = new ArrayList<>();

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = session
                    .createNativeQuery(selectMinMixValue)
                    .setParameter("date", date)
                    .getResultList();

            for (Object[] row : results) {
                String code = row[0].toString();
                double min = ((Number) row[1]).doubleValue();
                double max = ((Number) row[2]).doubleValue();
                boundsList.add(new MinMaxBound(code, min, max));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return boundsList;
    }



}
