package com.mommyce.appcore.strategy.barber.impl;

import com.mommyce.appcore.constant.ResponseStatus;
import com.mommyce.appcore.domain.barber.BarberTestimonial;
import com.mommyce.appcore.domain.common.ResultMessage;
import com.mommyce.appcore.strategy.barber.DeleteDataStrategy;
import com.mommyce.appcore.strategy.barber.GetDataStrategy;
import com.mommyce.appcore.strategy.barber.SaveOrUpdateDataStrategy;
import com.mommyce.appcore.strategy.common.impl.CommonStrategy;
import com.mommyce.appcore.utils.AppUtils;
import org.apache.juli.OneLineFormatter;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by israjhaliri on 8/28/17.
 */
@Repository
public class BarberTestimonialStrategy {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GetDataStrategy getDataStrategy;

    private SaveOrUpdateDataStrategy<BarberTestimonial> saveOrUpdateDataStrategy;

    private DeleteDataStrategy<Integer> deleteDataStrategy;

    @Autowired
    CommonStrategy commonStrategy;

    public List<BarberTestimonial> getListData() {
        getDataStrategy = (parameters) -> {
            List<BarberTestimonial> barberTestimonialList = new ArrayList<>();

            String sql = "SELECT * FROM barber.testimonial ORDER BY create_date DESC LIMIT 5";

            barberTestimonialList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BarberTestimonial.class));
            AppUtils.getLogger(this).debug("TESTIMONIAL LOG GET DATA: {}", barberTestimonialList.toString());
            return barberTestimonialList;
        };
        return (List<BarberTestimonial>) getDataStrategy.process(null);
    }

    public List<BarberTestimonial> getListDataById(Integer id) {
        getDataStrategy = (parameters) -> {
            List<BarberTestimonial> barberTestimonialList = new ArrayList<>();

            String sql = "SELECT * FROM barber.testimonial where id_testimonial = ?";

            barberTestimonialList = jdbcTemplate.query(sql,new Object[]{parameters}, new BeanPropertyRowMapper(BarberTestimonial.class));
            AppUtils.getLogger(this).debug("TESTIMONIAL LOG GET DATA: {}", barberTestimonialList.toString());
            return barberTestimonialList;
        };
        return (List<BarberTestimonial>) getDataStrategy.process(id);
    }

    public List<BarberTestimonial> getListDataPerPage(Object allparameters) {
        getDataStrategy = (parameters) -> {
            Map<String, Object> param = (Map<String, Object>) parameters;
            List<BarberTestimonial> barberTestimonialList = new ArrayList<>();

            String sql = "SELECT t.*\n" +
                    "FROM\n" +
                    "   (SELECT row_number() over() as rn,t.*\n" +
                    "       FROM\n" +
                    "           (SELECT t.*\n" +
                    "                   FROM\n" +
                    "                    (SELECT COUNT(id_testimonial) OVER() TOTAL_COUNT,id_testimonial,subject,description,age,create_date,update_date\n" +
                    "                    FROM barber.testimonial \n" +
                    "                    WHERE subject LIKE  '%" + param.get("search") + "%' \n" +
                    "                    ORDER BY barber.testimonial.id_testimonial DESC \n" +
                    "                    ) t\n" +
                    "                 ) t\n" +
                    "          ) t\n" +
                    "WHERE t.rn BETWEEN " + param.get("start") + "::integer AND " + param.get("length") + "::integer";
            AppUtils.getLogger(this).debug("GET TESTIMONIAL LOG : {}", barberTestimonialList.toString());
            return barberTestimonialList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BarberTestimonial.class));
        };
        return (List<BarberTestimonial>) getDataStrategy.process(allparameters);
    }


    @Transactional
    public void saveData(BarberTestimonial barberGuestBook) {
        saveOrUpdateDataStrategy = (BarberTestimonial parameters) -> {
            String sql = "INSERT INTO barber.testimonial\n" +
                    "(subject, description, age, create_date, update_date)\n" +
                    "VALUES (?, ?, ?, current_date, current_date)";
            jdbcTemplate.update(sql, parameters.getSubject(), parameters.getDescription(), parameters.getAge());
        };
        saveOrUpdateDataStrategy.process(barberGuestBook);
    }

    @Transactional
    public void updateData(BarberTestimonial barberTestimonial) {
        saveOrUpdateDataStrategy = (BarberTestimonial parameters) -> {
            String sql = "UPDATE barber.testimonial\n" +
                    "SET subject=?, description=?, age=?, update_date=current_date\n" +
                    "WHERE id_testimonial = ?";
            jdbcTemplate.update(sql, parameters.getSubject(), parameters.getDescription(), parameters.getAge(), parameters.getIdTestimonial());
        };
        saveOrUpdateDataStrategy.process(barberTestimonial);
    }

    @Transactional
    public void deleteData(Integer id) {
        deleteDataStrategy = (parameters) -> {
            String sql = "DELETE FROM barber.testimonial\n" +
                    "WHERE id_testimonial = ?";
            jdbcTemplate.update(sql, parameters);
        };
        deleteDataStrategy.process(id);
    }
}
