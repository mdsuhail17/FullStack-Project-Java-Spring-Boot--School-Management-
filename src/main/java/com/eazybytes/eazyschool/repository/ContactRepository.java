package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.rommappers.ContactRowMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/*
@Repository stereotype annotation is used to add a bean of this class
type to the Spring context and indicate that given Bean is used to perform
DB related operations and
* */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    //-- controlled by spring jpa and these are derived querry
       // Note :  before the pagenation we are extending the crudrepositary and now we are extending the JpaRepository for pagenation

    List<Contact> findByStatus(String status); //Derived Queerry Method



//    @Query("SELECT c FROM Contact c WHERE c.status = :status") // JPQL Querry first priority this after that derived querry
    @Query(value = "SELECT * FROM contact_msg c WHERE c.status = :status", nativeQuery = true) // native querry we use table names and coulumn names in database
    Page<Contact> findByStatusWithQuerry(@Param("status") String status, Pageable pageable); // Pagenation and dnamic sorting :-  //Page : wraping into the Page = because its return type is wrapped inside the page interface
          //     (@Param("status")  used for parameter match                  //----- controlled by spring jpa and these are derived querry

    // for detelete and upate and insert  updating the status
    @Transactional // for roleback and commits
    @Modifying  // for update and inster and delete
    @Query("UPDATE Contact c SET c.status = ?1 WHERE c.contactId =?2" )
    int updateStatusById(String status, int id);

    // Code related to spring JDBC this is commented because we are using Spring Data JPA
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public ContactRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public int saveContactMsg(Contact contact){
//        String sql = "insert into contact_msg (name,mobile_num,email,subject,message,status," +
//                "created_at,created_by) values (?,?,?,?,?,?,?,?)";
//        return jdbcTemplate.update(sql,contact.getName(),contact.getMobileNum(),
//                contact.getEmail(),contact.getSubject(),contact.getMessage(),
//                contact.getStatus(),contact.getCreatedAt(),contact.getCreatedBy());
//    }
//
//    public List<Contact> findMsgsWithStatus(String status) {
//        String sql = "select * from contact_msg where status = ?";
//        return jdbcTemplate.query(sql,new PreparedStatementSetter() {
//            public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                preparedStatement.setString(1, status);
//            }
//        },new ContactRowMapper());
//    }
//
//    public int updateMsgStatus(int contactId, String status,String updatedBy) {
//        String sql = "update contact_msg set status = ?, updated_by = ?,updated_at =? where contact_id = ?";
//        return jdbcTemplate.update(sql,new PreparedStatementSetter() {
//            public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                preparedStatement.setString(1, status);
//                preparedStatement.setString(2, updatedBy);
//                preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//                preparedStatement.setInt(4, contactId);
//            }
//        });
//    }

}
