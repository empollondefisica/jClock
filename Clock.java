
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class Clock extends Application
{
    public static void main( String[] args )
    {
        Application.launch( args );
    }

    public void start( Stage stage )
    {
        Group group = new Group();
        Scene scene = new Scene( group, 400, 400 );
        Pane pane = new Pane();
        Ellipse clockFace = new Ellipse();
        Circle centerCircle = new Circle();

        clockFace.centerXProperty().bind( scene.widthProperty().divide( 2.0 ) );
        clockFace.centerYProperty().bind( scene.heightProperty().divide( 2.0 ) );
        clockFace.radiusXProperty().bind( scene.widthProperty().divide( 2.0 ).subtract( 5.0 ) );
        clockFace.radiusYProperty().bind( scene.heightProperty().divide( 2.0 ).subtract( 5.0 ) );
        clockFace.setFill( Color.WHITE );

        centerCircle.centerXProperty().bind(clockFace.centerYProperty());
        centerCircle.centerYProperty().bind(clockFace.centerYProperty());
        centerCircle.setRadius( 10 );

        Line secondHand = new Line();
        Line minuteHand = new Line();
        Line hourHand = new Line();

        secondHand.setStartX( clockFace.getCenterX() );
        secondHand.setStartY( clockFace.getCenterY() );
        secondHand.setEndX( clockFace.getCenterX() );
        secondHand.setEndY( clockFace.getCenterY() - 160.0 );
        secondHand.setSmooth( true );

        minuteHand.setStartX( clockFace.getCenterX() );
        minuteHand.setStartY( clockFace.getCenterY() );
        minuteHand.setEndX( clockFace.getCenterX() );
        minuteHand.setEndY( clockFace.getCenterY() - 180.0 );
        minuteHand.setSmooth( true );
        minuteHand.setStrokeWidth(3);

        hourHand.setStartX( clockFace.getCenterX() );
        hourHand.setStartY( clockFace.getCenterY() );
        hourHand.setEndX( clockFace.getCenterX() );
        hourHand.setEndY( clockFace.getCenterY() - 100.0 );
        hourHand.setSmooth( true );
        hourHand.setStrokeWidth(5);

        pane.getChildren().add( clockFace );
        pane.getChildren().add( secondHand );
        pane.getChildren().add( minuteHand );
        pane.getChildren().add( hourHand );
        pane.getChildren().add( centerCircle );

        for( int i = 0; i < 12; i++ )
        {
            Line line = new Line();
            line.setStartX( clockFace.getCenterX() + 170.0 * Math.cos( i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setStartY( clockFace.getCenterY() + 170.0 * Math.sin( i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setEndX( clockFace.getCenterX() + 190.0 * Math.cos( i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setEndY( clockFace.getCenterY() + 190.0 * Math.sin( i * Math.PI / 6.0  - Math.PI / 2.0 ) );

            pane.getChildren().add( line );
        }

        pane.prefWidthProperty().bind( scene.widthProperty() );
        pane.prefHeightProperty().bind( scene.heightProperty() );
        pane.setStyle( "-fx-background-color: #000000" );

        group.getChildren().add( pane );

        stage.setResizable( false );
        stage.setTitle( "jClock" );
        stage.setScene( scene );
        stage.show();

        new AnimationTimer()
        {
            long second = 0;
            long minute = 0;
            long hour   = 0;
            long now    = 0;

            public void handle( long ignore )
            {
                now    = System.currentTimeMillis();
                second = ( now / 1000 ) % 60;
                minute = ( now / 1000 / 60 ) % 60;
                hour   = ( ( now / 1000 / 3600 ) - 4 ) % 12;

                secondHand.setEndX( clockFace.getCenterX() + 160 * Math.cos( second * Math.PI / 30.0 - Math.PI / 2.0 ) );
                secondHand.setEndY( clockFace.getCenterY() + 160 * Math.sin( second * Math.PI / 30.0 - Math.PI / 2.0 ) );
                minuteHand.setEndX( clockFace.getCenterX() + 180 * Math.cos( ( minute * 60 + second ) * Math.PI / 30.0 / 60.0 - Math.PI / 2.0 ) );
                minuteHand.setEndY( clockFace.getCenterY() + 180 * Math.sin( ( minute * 60 + second ) * Math.PI / 30.0 / 60.0 - Math.PI / 2.0 ) );
                hourHand.setEndX( clockFace.getCenterX() + 100 * Math.cos( ( hour * 3600 + minute * 60 + second ) * Math.PI / 6.0 / 60.0 / 60.0 - Math.PI / 2.0 ) );
                hourHand.setEndY( clockFace.getCenterY() + 100 * Math.sin( ( hour * 3600 + minute * 60 + second ) * Math.PI / 6.0 / 60.0 / 60.0 - Math.PI / 2.0 ) );
            }
        }.start();
    }
}
