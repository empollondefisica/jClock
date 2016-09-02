import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.stage.StageStyle;

public class jClock extends Application
{
    private boolean moving;
    private double  opacity;
    private double  startX;
    private double  startY;

    public static void main( String[] args )
    {
        Application.launch( args );
    }

    public void start( Stage stage )
    {
        moving  = false;
        opacity = 0.80;

        final Group   group      = new Group();
        final Scene   scene      = new Scene( group, 400, 400 );
        final Pane    pane       = new Pane();
        final Ellipse clockFace  = new Ellipse();
        final Line    secondHand = new Line();
        final Line    minuteHand = new Line();
        final Line    hourHand   = new Line();
        final Label   quitLabel  = new Label("X");

        clockFace.centerXProperty().bind( scene.widthProperty().divide( 2.0 ) );
        clockFace.centerYProperty().bind( scene.heightProperty().divide( 2.0 ) );
        clockFace.radiusXProperty().bind( scene.widthProperty().divide( 2.0 ).subtract( 5.0 ) );
        clockFace.radiusYProperty().bind( scene.heightProperty().divide( 2.0 ).subtract( 5.0 ) );
        clockFace.setFill( Color.WHITE );
        clockFace.setStroke(Color.BLACK);
        clockFace.setFill(Color.rgb(255,255,255,opacity));
        clockFace.setEffect(new DropShadow());

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

        hourHand.setStartX( clockFace.getCenterX() );
        hourHand.setStartY( clockFace.getCenterY() );
        hourHand.setEndX( clockFace.getCenterX() );
        hourHand.setEndY( clockFace.getCenterY() - 100.0 );
        hourHand.setSmooth( true );

        pane.getChildren().add( clockFace );
        pane.getChildren().add( secondHand );
        pane.getChildren().add( minuteHand );
        pane.getChildren().add( hourHand );
        pane.getChildren().add( quitLabel );

        for( int i = 0; i < 12; i++ )
        {
            Line line = new Line();
            line.setStartX( clockFace.getCenterX() + 170.0 * Math.cos( (double)i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setStartY( clockFace.getCenterY() + 170.0 * Math.sin( (double)i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setEndX( clockFace.getCenterX() + 190.0 * Math.cos( (double)i * Math.PI / 6.0  - Math.PI / 2.0 ) );
            line.setEndY( clockFace.getCenterY() + 190.0 * Math.sin( (double)i * Math.PI / 6.0  - Math.PI / 2.0 ) );

            pane.getChildren().add( line );
        }

        quitLabel.setShape(new Circle(0, 0, 25));
        quitLabel.setEffect(new DropShadow());
        quitLabel.setMinWidth(25);
        quitLabel.setMinHeight(25);
        quitLabel.setAlignment(Pos.CENTER);
        quitLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        quitLabel.setOnMouseEntered(buildMouseEnteredEventHandler(stage, quitLabel));
        quitLabel.setOnMouseClicked(buildMouseClickedEventHandler(stage, quitLabel));
        quitLabel.setOnMouseExited(buildMouseExitedEventHandler(stage, quitLabel));

        pane.prefWidthProperty().bind( scene.widthProperty() );
        pane.prefHeightProperty().bind( scene.heightProperty() );
        pane.setStyle( "-fx-background-color: rgba(0,0,0,0.0)" );
        pane.setBackground(Background.EMPTY);
        pane.setOnMousePressed(buildMousePressedEventHandler(stage, clockFace));
        pane.setOnMouseDragged(buildMouseDraggedEventHandler(stage, clockFace));
        pane.setOnMouseReleased(buildMouseReleasedEventHandler(stage,clockFace));
        pane.setOnScroll(buildScrollEventHandler(clockFace));

        group.getChildren().add( pane );

        scene.setFill(Color.rgb(0,0,0,0.0));

        stage.getIcons().setAll(new Image("file:resources/clock_icon.png"));
        stage.setResizable( false );
        stage.setTitle("jClock");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
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

                Platform.runLater(new Runnable()
                {
                    public void run()
                    {
                        secondHand.setEndX(clockFace.getCenterX() + 160.0 * Math.cos(second * Math.PI / 30.0 - Math.PI / 2.0));
                        secondHand.setEndY(clockFace.getCenterY() + 160.0 * Math.sin(second * Math.PI / 30.0 - Math.PI / 2.0));
                        minuteHand.setEndX(clockFace.getCenterX() + 180.0 * Math.cos((minute * 60 + second) * Math.PI / 30.0 / 60.0 - Math.PI / 2.0));
                        minuteHand.setEndY(clockFace.getCenterY() + 180.0 * Math.sin((minute * 60 + second) * Math.PI / 30.0 / 60.0 - Math.PI / 2.0));
                        hourHand.setEndX(clockFace.getCenterX() + 100.0 * Math.cos((hour * 3600 + minute * 60 + second) * Math.PI / 6.0 / 60.0 / 60.0 - Math.PI / 2.0));
                        hourHand.setEndY(clockFace.getCenterY() + 100.0 * Math.sin((hour * 3600 + minute * 60 + second) * Math.PI / 6.0 / 60.0 / 60.0 - Math.PI / 2.0));
                    }
                });
            }
        }.start();
    }

    private EventHandler<MouseEvent> buildMouseReleasedEventHandler(final Stage stage, final Ellipse clockFace)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                moving = false;
                mouseEvent.consume();
            }
        };
    }

    private EventHandler<MouseEvent> buildMousePressedEventHandler(final Stage stage, final Ellipse clockFace)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                moving = true;
                startX = mouseEvent.getSceneX();
                startY = mouseEvent.getSceneY();
                mouseEvent.consume();
            }
        };
    }

    private EventHandler<MouseEvent> buildMouseEnteredEventHandler(final Stage stage, final Label quitLabel)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                quitLabel.setFont(Font.font(15));
            }
        };
    }

    private EventHandler<MouseEvent> buildMouseClickedEventHandler(final Stage stage, final Label quitLabel)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                Platform.exit();
            }
        };
    }

    private EventHandler<MouseEvent> buildMouseExitedEventHandler(final Stage stage, final Label quitLabel)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                quitLabel.setFont(Font.font(12));
            }
        };
    }

    private EventHandler<MouseEvent> buildMouseDraggedEventHandler(final Stage stage, final Ellipse clockFace)
    {
        return new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                if (moving)
                {
                    stage.setX(mouseEvent.getScreenX() - startX);
                    stage.setY(mouseEvent.getScreenY() - startY);
                }
                mouseEvent.consume();
            }
        };
    }

    private EventHandler<ScrollEvent> buildScrollEventHandler(final Ellipse clockFace)
    {
        return new EventHandler<ScrollEvent>()
        {
            public void handle(ScrollEvent scrollEvent)
            {
                if(scrollEvent.getDeltaY() > 0)
                {
                    if(opacity < 0.90)
                    {
                        opacity += 0.1;
                    }
                    else
                    {
                        opacity = 1.0;
                    }
                }
                else
                {
                    if(opacity > 0.2)
                    {
                        opacity -= 0.1;
                    }
                }
                clockFace.setFill(Color.rgb(255,255,255,opacity));
                scrollEvent.consume();
            }
        };
    }
}
