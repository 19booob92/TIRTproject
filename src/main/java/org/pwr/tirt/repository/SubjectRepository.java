package org.pwr.tirt.repository;

import org.pwr.tirt.model.dto.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long>{

}
