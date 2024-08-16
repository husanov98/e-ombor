package uz.mh.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mh.eombor.model.NewClientInfo;

public interface NewClientInfoRepository extends JpaRepository<NewClientInfo,Long> {
}
