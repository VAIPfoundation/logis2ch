package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_INFO;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX2;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX3;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX4;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX5;
import com.sdc2ch.web.admin.repo.domain.v.V_VRO_OD_MATRIX;

public interface RoutePathInfoService {
	List<T_ROUTE_PATH_INFO> findAll();
	List<T_ROUTE_PATH_MATRIX> findMatirxAll();
	List<T_ROUTE_PATH_MATRIX2> findMatirx2All();
	
	public void save(T_ROUTE_PATH_MATRIX5 s);
	public void save(T_ROUTE_PATH_MATRIX3 s);
	public void save(T_ROUTE_PATH_MATRIX2 s);
	public void save(T_ROUTE_PATH_MATRIX s);
	public void save(T_ROUTE_PATH_INFO s);
	List<T_ROUTE_PATH_MATRIX3> findMatirx3All();
	List<T_ROUTE_PATH_MATRIX3> findMatirx3();
	List<T_ROUTE_PATH_MATRIX3> listRoutePathInfo(String month);
	List<T_ROUTE_PATH_MATRIX3> listRoutePathInfo(String month, String fctryCd, String RouteNo, String vrn, String changeBase);
	T_ROUTE_PATH_MATRIX3 searchRoutePathInfo(Long id);
	
	List<T_ROUTE_PATH_MATRIX4> listRoutePathInfo4(String month);
	List<T_ROUTE_PATH_MATRIX4> listRoutePathInfo4(String month, String fctryCd, String RouteNo, String vrn, String changeBase, String routeTy2);
	T_ROUTE_PATH_MATRIX4 searchRoutePathInfo4(Long id);

	V_VRO_OD_MATRIX searchTmsRoutePathInfo(String startCd, String endCd);
	List<T_ROUTE_PATH_MATRIX5> findMatirx5All();
	List<T_ROUTE_PATH_MATRIX5> findMatirx5();
	List<T_ROUTE_PATH_MATRIX5> findMatirx5(String dest);
}
