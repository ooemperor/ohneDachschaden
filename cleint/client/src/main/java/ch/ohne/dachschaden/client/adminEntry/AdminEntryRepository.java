package ch.ohne.dachschaden.client.adminEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminEntryRepository extends JpaRepository<AdminEntry, Long> {

}
