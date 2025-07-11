package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Users;

public class UsersRegister {


  @NotBlank(message = "Campo nome é obrigatorio")
	private String name;
  @NotBlank(message = "Campo email é obrigatorio")
	private String email;
  @NotBlank(message = "Campo password é obrigatorio")
	private String password;
  @NotBlank(message = "Campo aplication é obrigatorio")
	private String aplication;

	public Users toDate() {
		
		String password = new BCryptPasswordEncoder().encode(this.password);
		
		Users user = new Users(this.name, this.email,  password, this.aplication);
		return user;
	}

	public String getEmail() {
		return email;
	}


	public String getPassword() {
		return password;
	}


	public String getAplication() {
		return aplication;
	}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAplication(String aplication) {
    this.aplication = aplication;
  }


}
