package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdc2ch.web.admin.repo.domain.T_MENU;

public class PARSING {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		java.io.InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("menu2.json");
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		String s;
		
		StringBuffer sb = new StringBuffer();
		try {
			while((s=in.readLine()) != null) {
				sb.append(s);

			}
			
			GsonBuilder gsonBuilder = new GsonBuilder();  
			gsonBuilder.serializeNulls();  
			Gson gson = gsonBuilder.create();
			
			T_MENU root = new T_MENU();
			
			root.setPath("#");
			root.setTitle("root");

			
			gson.fromJson(sb.toString(), List.class).forEach( el -> {
				

				
				T_MENU m = gson.fromJson(gson.toJsonTree(el), T_MENU.class);
				m.setParent(root);
				root.getItems().add(m);
				
				m.getItems().forEach(m1 -> {
					m1.setParent(m);
					
					if(m1.getItems() != null) {
						
						m1.getItems().forEach(m2 -> {
							m2.setParent(m1);
							
						});
						
					}
				});

				
				
				
				m.getItems().forEach(m1 -> {

					if(m1.getItems() != null) {
						
						m1.getItems().forEach(m2 -> {

							
						});
						
					}
					
				});

				
			});;
			

			
			
			
			

			
			
			List<T_MENU> ms = root.flattened().filter(m -> filter(m)).collect(Collectors.toList());
			
			
			System.out.println(ms);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	static List<Long> list = Arrays.asList(1L, 2L, 10L);
	private static boolean filter(T_MENU m) {
		
		
		return list.contains(m.getId());
	}
	
	
}
