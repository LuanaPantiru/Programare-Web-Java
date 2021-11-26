package demo;

import java.util.UUID;

public class TaskModel implements BaseModel{

    public enum TaskStatus{
        OPEN,
        IN_PROGRESS,
        CLOSED
    }
    public enum TaskServerity{
        LOW,
        NORMAL,
        HIGH
    }
    private String id;
    private String title;
    private String description;
    private String assignedTo;
    private TaskStatus taskStatus;
    private TaskServerity taskServerity;

    public TaskModel(){
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskServerity getTaskServerity() {
        return taskServerity;
    }

    public void setTaskServerity(TaskServerity taskServerity) {
        this.taskServerity = taskServerity;
    }

    public void update(TaskModel taskModel){
        if(taskModel != null){
            title = taskModel.getTitle();
            description = taskModel.getDescription();
            assignedTo = taskModel.getAssignedTo();
            taskServerity = taskModel.getTaskServerity();
            taskStatus = taskModel.getTaskStatus();
        }
    }

    public void patch(TaskModel taskModel){
        if(taskModel != null){
            if(taskModel.getTitle() != null){
                title = taskModel.getTitle();
            }
            if(taskModel.getDescription() != null){
                description = taskModel.getDescription();
            }
            if(taskModel.getAssignedTo() != null){
                assignedTo = taskModel.getAssignedTo();
            }
            if(taskModel.getTaskStatus() != null){
                taskStatus = taskModel.getTaskStatus();
            }
            if(taskModel.getTaskServerity() != null){
                taskServerity = taskModel.getTaskServerity();
            }
        }
    }
}
