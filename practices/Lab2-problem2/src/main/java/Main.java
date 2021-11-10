import controllers.FileController;
import interfaces.FilesController;
import models.dto.Dimensions;
import models.dto.Duration;
import models.entityclasses.DocumentFile;
import models.entityclasses.ImageFile;
import models.entityclasses.MultimediaFile;
import models.entityclasses.VideoFile;
import models.enums.FileFormats;
import models.repository.FileRepository;
import models.templateclasses.AbstractFile;
import views.ConsoleView;

public class Main {
    public static void main(String[] args) {
        {
            System.out.println("\nExample with \"AbstractFile\"-repository...");

            FileRepository repository = new FileRepository(new AbstractFile[10]); //тут работает type inference

            ConsoleView view = new ConsoleView(20, 20, 30);
            view.registerSource(repository);

            FilesController controller = new FileController();
            controller.registerModel(repository);
            controller.registerView(view);

            controller.appendData(
                    new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
            controller.appendData(
                    new DocumentFile("file 2.xlsx", FileFormats.XLSX, 1231231, 423));
            controller.appendData(
                    new DocumentFile("very looooong filename 281583581.docx",
                            FileFormats.DOCX, 1231231, 4814));
            controller.appendData(new ImageFile("spb-map.png", FileFormats.PNG, 703527,
                    new Dimensions(1024, 3072)));
            controller.appendData(
                    new ImageFile("spb-map with long long name with many many words.jpg",
                            FileFormats.JPG, 703527, new Dimensions(1024, 3072)));
            controller.appendData(
                    new ImageFile("spb-map with long long name with many many words 2 (to print in again).png",
                            FileFormats.PNG, 703527, new Dimensions(1024, 3072)));
            controller.appendData(
                    new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                            "Eric Clapton, Pretty Girl", new Duration(5, 28)));
            controller.appendData(
                    new MultimediaFile("TrackNo-06.Artist-Eric Clapton.Album-Money and Cigarettes." +
                            "Song-PrettyGirl.Year-1983.acc", FileFormats.ACC, 7893454,
                            "Eric Clapton, Pretty Girl", new Duration(5, 28)));
            controller.appendData(
                    new VideoFile("BackToTheFuture1.avi", FileFormats.AVI, 1470984192,
                            "Back to the future I, 1985", new Duration(1, 48, 8),
                            new Dimensions(640, 352)));
            controller.appendData(
                    new VideoFile("BackToTheFuture2.mp4", FileFormats.MP4, 1470984192,
                            "Back to the future II with very very long description of the movie with " +
                                    "many unnecessary details", new Duration(1, 48, 8),
                            new Dimensions(640, 352)));

            controller.printAll();
        }
        {
            System.out.println("Example with \"MultimediaFile\"-repository...");

            FileRepository repository = new FileRepository(new MultimediaFile[10]); //тут работает type inference

            ConsoleView view = new ConsoleView(20, 20, 30);
            view.registerSource(repository);

            FilesController controller = new FileController();
            controller.registerModel(repository);
            controller.registerView(view);

            controller.appendData(
                    new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                            "Eric Clapton, Pretty Girl", new Duration(5, 28)));
            controller.appendData(
                    new MultimediaFile("TrackNo-06.Artist-Eric Clapton.Album-Money and Cigarettes." +
                            "Song-PrettyGirl.Year-1983.mp3", FileFormats.MP3, 7893454,
                            "Eric Clapton, Pretty Girl", new Duration(5, 28)));
            controller.appendData(
                    new VideoFile("BackToTheFuture1.avi", FileFormats.AVI, 1470984192,
                            "Back to the future I, 1985", new Duration(1, 48, 8),
                            new Dimensions(640, 352)));
            controller.appendData(
                    new VideoFile("BackToTheFuture2.avi", FileFormats.AVI, 1470984192,
                            "Back to the future II with very very long description of the movie with " +
                                    "many unnecessary details", new Duration(1, 48, 8),
                            new Dimensions(640, 352)));

            controller.printAll();
        }
    }
}
