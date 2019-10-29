package io.hashimati.usersservices.repository; 

import io.hashimati.usersservices.domains.*;
import io.micronaut.data.repository.CrudRepository;

public interface RolesRepository  extends CrudRepository<Role, Long> {

}
