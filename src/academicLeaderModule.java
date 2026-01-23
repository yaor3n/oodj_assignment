//Module
public class academicLeaderModule {
    private String code, name, qualification, lecturer, description;
    
    public academicLeaderModule(String code, String name, String qualification, String lecturer, String description){
        this.code = code;
        this.name = name;
        this.qualification = qualification;
        this.lecturer = lecturer;
        this.description = description;
    }
    
    //Getters
    public String getCode(){ return code; }
    public String getName(){ return name; }
    
    @Override
    public String toString() {
        return code + "," + name + "," + qualification + "," + lecturer + "," + description;
    }
}
