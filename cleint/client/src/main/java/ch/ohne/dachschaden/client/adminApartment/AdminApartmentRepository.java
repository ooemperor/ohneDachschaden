package ch.ohne.dachschaden.client.adminApartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminApartmentRepository extends JpaRepository<AdminApartment, Long> {

}
