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
public class Member {
	private String userId;
	private String userPwd;
	private String userEmail;
	private Timestamp registerDate;
	private String newFileName;
	private int userPoint;
	private String isAdmin;
}
