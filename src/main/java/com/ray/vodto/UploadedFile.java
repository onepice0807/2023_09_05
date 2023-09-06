package com.ray.vodto;

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
public class UploadedFile {
	private int no;
	private String originalFileName;
	private String ext;
	private String newFileName;
	private long size;
	private int boardNo;
	private String base64String;
	private String thumbFileName;
	
	
	
}
