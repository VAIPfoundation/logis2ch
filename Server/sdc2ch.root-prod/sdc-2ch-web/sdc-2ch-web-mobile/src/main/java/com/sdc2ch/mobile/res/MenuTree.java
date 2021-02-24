package com.sdc2ch.mobile.res;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MenuTree extends ResourceSupport {
	List<MobileMenu> menus;

}
