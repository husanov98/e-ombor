package uz.mh.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mh.eombor.model.OldStir;

import java.util.List;

public interface OldStirRepository extends JpaRepository<OldStir,Long> {
    @Query(nativeQuery = true,value = "select exists(select from old_stir where stir =:stir)")
    boolean isExists(@Param("stir") String stir);

    @Query(nativeQuery = true,value = "select * from old_stir where id between :startId and :endId")
    List<OldStir> getStirs(@Param("startId") Long startId, @Param("endId") Long endId);
}
