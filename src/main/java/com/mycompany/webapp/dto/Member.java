package com.mycompany.webapp.dto;


import lombok.Data;

@Data
public class Member { //회원 
   private String mid; //아이디(이메일)
   private String mpassword; //비밀번호
   private String mname; //이름
   private String mphonenum; //핸드폰 번호
   private String mnickname; //닉네임
   private String mrole; //롤
   private boolean menabled; //활성화 여부
   private byte[] mimgdata; //이미지 파일
   private String mimgoname; //이미지 파일 이름
   private String mimgtype; //이미지 파일 타입
}