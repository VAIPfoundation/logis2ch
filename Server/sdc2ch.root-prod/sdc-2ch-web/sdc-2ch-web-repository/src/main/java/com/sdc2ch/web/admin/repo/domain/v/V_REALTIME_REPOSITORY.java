package com.sdc2ch.web.admin.repo.domain.v;

import org.springframework.data.jpa.repository.JpaRepository;

public interface V_REALTIME_REPOSITORY extends JpaRepository<V_REALTIME_INFO, Long>{

	V_REALTIME_INFO findByVrn(String vrn);

}
