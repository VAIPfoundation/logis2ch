package test; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sdc2ch.web.admin.repo.dao.T_RoleRepository;
import com.sdc2ch.web.admin.repo.domain.T_ROLE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

public class RemoveTest {
	
	public static void main(String[] args) {
		Set<T_ROLE> roles = setupRole(null).stream().collect(Collectors.toSet());
		
		
		roles.removeIf(r -> {
			System.out.println(r.getRole());
			return !(r.getRole() == RoleEnums.SYSTEM || r.getRole() == RoleEnums.SUPER || r.getRole() == RoleEnums.FACTORY);
		} );
		
		
		System.out.println(roles);
		
		

		test(1);
		test(2);
		test(3);
		test(4);
		test(5);
		test(6);
		test(7);
		test(8);
		test(9);
		test(0);
	}
	
	public static List<T_ROLE> setupRole(T_RoleRepository repo) {
		
		List<T_ROLE> roles = new ArrayList<T_ROLE>();
		
		for(RoleEnums r : RoleEnums.values()) {
			
			T_ROLE role = new T_ROLE();
			role.setRole(r);
			role.setDesc(r.getDesc());

			
			roles.add(role);
			
		}
		
		return roles;

	}
	
	
	public  static void test(int idx) {
		
		List<Integer> ints = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
		
		
		System.out.println(ints.stream().anyMatch(i -> i == idx));
		
	}

}
