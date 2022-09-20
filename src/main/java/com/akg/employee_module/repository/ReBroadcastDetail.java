package com.akg.employee_module.repository;

import com.akg.employee_module.enums.Status;
import com.akg.employee_module.model.BroadcastDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public interface ReBroadcastDetail extends JpaRepository<BroadcastDetail,Long> {

    int countAllByBroadcastHeaderIdAndCompanyIdAndMediaId(Long broadcastHeaderId,
                                                          Long companyId,Long mediaId);

    int countAllByBroadcastHeaderIdAndMediaIdAndStatus(Long broadcastHeaderId,
                                                       Long mediaId, Status status);

    @Query(value = " select distinct company_id from broadcast_detail " +
            " where broadcast_header_id = ?1 ", nativeQuery = true)
    ArrayList<Long> getDistinctCompanyId(Long broadcastHeaderId);

    @Query(value = " select distinct media_id from broadcast_detail " +
            " where broadcast_header_id = ?1 ", nativeQuery = true)
    ArrayList<Long> getDistinctMediaId(Long broadcastHeaderId);

    ArrayList<BroadcastDetail> findAllByBroadcastHeaderIdAndCompanyIdAndMediaIdOrderByIdAsc
            (Long broadcastHeaderId,Long companyId,Long mediaId, Pageable pageable);

    @Query(value = "select * from broadcast_detail " +
            "where media_code = ?1 and api_response like concat('%',?2,'%') ",
            nativeQuery = true)
    ArrayList<BroadcastDetail> getBroadcastDetailByTypeAndMsgId(String type,String msgId);

}
