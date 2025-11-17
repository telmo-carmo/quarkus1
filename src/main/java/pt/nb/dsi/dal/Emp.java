package pt.nb.dsi.dal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMP")
public class Emp {

	@Id
	@Column(name = "EMPNO", nullable = false)
	private Integer empno;

	@Column(name = "ENAME", length = 10)
	private String ename;

	@Column(name = "JOB", length = 9)
	private String job;

	@Column(name = "MGR")
	private Integer mgr;

	@Column(name = "HIREDATE")
	private LocalDate hiredate;

	@Column(name = "SAL", precision = 7, scale = 2)
	private BigDecimal sal;

	@Column(name = "COMM", precision = 7, scale = 2)
	private BigDecimal comm;

	@Column(name = "DEPTNO")
	private Integer deptno;

	@Column(name = "TSTAMP")
	private LocalDateTime tstamp;

	// Getters and setters

	public Integer getEmpno() {
		return empno;
	}

	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Integer getMgr() {
		return mgr;
	}

	public void setMgr(Integer mgr) {
		this.mgr = mgr;
	}

	public LocalDate getHiredate() {
		return hiredate;
	}

	public void setHiredate(LocalDate hiredate) {
		this.hiredate = hiredate;
	}

	public BigDecimal getSal() {
		return sal;
	}

	public void setSal(BigDecimal sal) {
		this.sal = sal;
	}

	public BigDecimal getComm() {
		return comm;
	}

	public void setComm(BigDecimal comm) {
		this.comm = comm;
	}

	public Integer getDeptno() {
		return deptno;
	}

	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}

	public LocalDateTime getTstamp() {
		return tstamp;
	}

	public void setTstamp(LocalDateTime tstamp) {
		this.tstamp = tstamp;
	}
}
