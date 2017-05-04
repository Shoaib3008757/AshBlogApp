package ashir.ashblogapp;



public class AshirBlogPostingHelper {
    String dataPost;
    byte[] image;
    boolean isValueChecked;

    public void setIsValueChecked(boolean isChecked) {
        this.isValueChecked = isChecked;
    }



    public boolean isValueChecked() {
        return isValueChecked;
    }



    public void setDataPost(String post){
        this.dataPost = post;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
    public String getPost(){
        return dataPost;
    }
    public byte[] getImage(){
        return image;
    }

}
