package ch.ohne.dachschaden.client.adminBuilding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBuildingRepository extends JpaRepository<AdminBuilding, Long> {
}
