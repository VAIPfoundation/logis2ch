package com.sdc2ch.web.admin.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortableReq {
	private String field;
	private String dir;
}
