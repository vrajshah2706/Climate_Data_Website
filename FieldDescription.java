package app;

//class represening Level 1 C data which is to give field and its description
public class FieldDescription {
    //field
    private String Field;
    //description
    private String Description;

//set the fields
public FieldDescription(String Field, String Description){
    this.Field = Field;
    this.Description = Description;
}

//get fields
public String getField() {
    return Field;
}
public String getDescription() {
    return Description;
}

}
