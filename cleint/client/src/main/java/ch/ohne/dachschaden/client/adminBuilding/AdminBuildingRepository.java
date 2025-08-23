package ch.ohne.dachschaden.client.adminBuilding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminBuildingRepository extends JpaRepository<AdminBuilding, Long> {
    AdminBuilding findAdminBuildingByEGID(String egid);
}
