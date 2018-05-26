package com.auditing.work.jpa.po;

import java.io.Serializable;
import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@Table(name = "department")
@ApiModel(value = "部门",description="部门")
public class Department implements Serializable {
			private static final long serialVersionUID = 1L;

	
	
	    @Override
			public String toString() {
				return "Department [id=" + id + ", name=" + name + "]";
			}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @ApiModelProperty(value = "部门ID")
	    private Long id;

	    public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Column(name = "name")
	    @ApiModelProperty(value = "部门名称")
	    private String name;
	    
	    
	    
}