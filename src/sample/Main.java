package sample;

import javafx.application.Application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    VideoCapture videoCapture;
    private ScheduledExecutorService timer;


    @Override
    public void start(Stage primaryStage) throws Exception{






        BorderPane root = new BorderPane();
        ImageView imageView1 = new ImageView();
        root.setLeft(imageView1);
        primaryStage.setTitle("Grey scale");
        primaryStage.setScene(new Scene(root, 900, 400));
        test(imageView1);
        primaryStage.show();








    }


    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);


    }


    void test (ImageView imageView){
         this.videoCapture = new VideoCapture();
         videoCapture.open(0);

      Runnable runnable = new Runnable() {
          @Override
          public void run() {
              Mat frame = grabFrame();
                  videoCapture.read(frame);

                  Image image= Utils.mat2Image(frame);
                 updateImageView(imageView,image);



          }
      };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(runnable, 0, 20, TimeUnit.MILLISECONDS);

    }




    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if ( this.videoCapture.isOpened()) {
            try {
                // read the current frame
                this.videoCapture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }
    private void updateImageView(ImageView view, Image image)
    {
        Utils.onFXThread(view.imageProperty(), image);
    }

}
