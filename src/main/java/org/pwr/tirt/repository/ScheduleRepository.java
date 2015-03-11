package org.pwr.tirt.repository;

import java.util.List;

import org.pwr.tirt.model.ProcessedSchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ProcessedSchedule, Long>{

    List<ProcessedSchedule> findByIndexNo(Long indexNo);
    
}
