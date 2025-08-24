package ch.ohne.dachschaden.client.adminCodes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCodesRepository extends JpaRepository<AdminCodes, Long> {

}
