package com.auditing.work.dal.dataobject;
import java.util.List;
/**
 * Created by innolab on 17-1-2.
 */
public class ReviewPointsReturn {
    private Integer id1;



    @Override
	public String toString() {
		return "ReviewPointsReturn [id1=" + id1 + ", level1=" + level1 + ", name1=" + name1 + ", level2=" + level2
				+ ", name2=" + name2 + ", id2=" + id2 + ", level3=" + level3 + ", name3=" + name3 + ", id3=" + id3
				+ ", level4=" + level4 + ", name4=" + name4 + ", id4=" + id4 + ", total_score=" + total_score
				+ ", userName=" + userName + "]";
	}

	public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }



    public String getLevel3() {
        return level3;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }



    public String getLevel4() {
        return level4;
    }

    public void setLevel4(String level4) {
        this.level4 = level4;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public Integer getId4() {
        return id4;
    }

    public void setId4(Integer id4) {
        this.id4 = id4;
    }

    public List<ReviewPoint> getReviewPoints() {
        return reviewPoints;
    }

    public void setReviewPoints(List<ReviewPoint> reviewPoints) {
        this.reviewPoints = reviewPoints;
        this.reviewPoints.forEach(reviewPoint->{
        	reviewPoint.setLevel1Name(name1);
        	reviewPoint.setLevel2Name(name2);
        	reviewPoint.setLevel3Name(name3);
        	reviewPoint.setLevel4Name(name4);
        });
    }

    private String level1;
    private String name1;

    private String level2;
    private String name2;
    private Integer id2;

    private String level3;
    private String name3;

    public Integer getId3() {
        return id3;
    }

    public void setId3(Integer id3) {
        this.id3 = id3;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    private Integer id3;

    private String level4;
    private String name4;
    private Integer id4;

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getUserName() {
    	
    	if (this.userName == null) {
			return  "";
		}else{
			return userName;
		}
		
	}

	public void setUserName(String userName) {
		
			this.userName = userName;
		
		
	}

	private String total_score;
    private String userName;
    List<ReviewPoint> reviewPoints;
}
