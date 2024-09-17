package uz.mh.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mh.eombor.model.NewClientInfo;

import java.util.List;

public interface NewClientInfoRepository extends JpaRepository<NewClientInfo,Long> {
    @Query(nativeQuery = true,value = "select stir from new_client_info where id between :startId and :endId")
    List<String> getStirs(@Param("startId") Long startId,@Param("endId") Long endId);
}
