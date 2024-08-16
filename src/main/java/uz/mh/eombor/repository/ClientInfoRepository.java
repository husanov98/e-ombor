package uz.mh.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.model.ClientInfo;

import java.util.List;

public interface ClientInfoRepository extends JpaRepository<ClientInfo,Long> {
    @Query(nativeQuery = true,value = "select * from client_info c where c.stir not in :stirs order by c.id limit 10000")
    List<ClientInfo> getDiffData(@Param("stirs")List<String> stirs);
}
