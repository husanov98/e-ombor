package uz.mh.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mh.eombor.model.ClientInfo;

public interface ClientInfoRepository extends JpaRepository<ClientInfo,Long> {
}
