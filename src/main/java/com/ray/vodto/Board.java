package com.ray.vodto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Board {
	private int no;
	private String writer;
	private String title;
	private Timestamp postDate;
	private String content;
	private int readcount;
	private int likecount;
	private int ref;
	private int step;
	private int reforder;
	private String isDelete;
}
