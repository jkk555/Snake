import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

class Jedzenie {
    Image image = new Image("banan.png");
    Image image2 = new Image("grzyb.jpg");
    Image image3 = new Image("monster.jpg");
    ImageView imageView = new ImageView(image);
    long czasZnikniecia;
    int czasNaEkranie = 10000;
    int typ=0;
    void setX(int szerokosc){
        imageView.setX(new Random().nextInt(szerokosc-60)+10);
        System.out.println("BananX"+imageView.getX());
    }

    void setY(int wysokosc){
        imageView.setY(new Random().nextInt(wysokosc-60)+10);
        System.out.println("BananY"+imageView.getY());
    }


    public void newLocation(int wysokosc, int szerokosc){
        if (czasZnikniecia<System.currentTimeMillis()){
            this.setX(szerokosc);
            this.setY(wysokosc);
            czasZnikniecia = System.currentTimeMillis()+czasNaEkranie;
            this.newFood();
        }
    }

    public void newFood(){
        typ= (new Random().nextInt(100)+1)%5;
        switch (typ){
            case 0:   //banan
                imageView.setImage(image);
                czasNaEkranie=10000;
                break;
            case 1:
                imageView.setImage(image2);
                czasNaEkranie=15000;
                break;
            case 2:
                imageView.setImage(image3);
                czasNaEkranie=15000;
                break;
            case 3:   //banan
                imageView.setImage(image);
                czasNaEkranie=10000;
                break;
            case 4:   //banan
                imageView.setImage(image);
                czasNaEkranie=10000;
                break;
        }
    }

    public Jedzenie(int szerokosc,int wysokosc){
        setX(szerokosc);
        setY(wysokosc);
        czasZnikniecia = System.currentTimeMillis()+czasNaEkranie;
        imageView.setScaleX(0.15);
        imageView.setScaleY(0.15);

    }


}

class Waz {
    public int szerokoscOkna=600, wysokoscOkna=600;

    ArrayList<Circle> segment = new ArrayList<>();
    private double x=200, y=200, r=10;
    private int kierunek;
    public Waz(){
        segment.add(new Circle(r,Color.BLACK));
        segment.get(0).setCenterX(x);
        segment.get(0).setCenterY(y);
        kierunek = 2;
    }
    public Circle getSegment(int nr){
      return segment.get(nr);
    }

    public int getDlugosc() {
        return segment.size();
    }

    public void addSegment(){
      double x=segment.get(segment.size()-1).getCenterX(),y=segment.get(segment.size()-1).getCenterY();
      segment.add(new Circle(r,Color.YELLOW));
      //Right
      if (kierunek==1){
          segment.get(segment.size()-1).setCenterX(x-(2*r));
          segment.get(segment.size()-1).setCenterY(y);

      }
      //Left
      if (kierunek==3){
            segment.get(segment.size()-1).setCenterX(x+(2*r));
            segment.get(segment.size()-1).setCenterY(y);

      }
      //Up
      if (kierunek==0){
            segment.get(segment.size()-1).setCenterX(x);
            segment.get(segment.size()-1).setCenterY(y+(2*r));

      }
      //Down
      if (kierunek==2){
            segment.get(segment.size()-1).setCenterX(x);
            segment.get(segment.size()-1).setCenterY(y-(2*r));

      }
    }
    public void move(){
        for (int i=segment.size()-1;i>=1;i--){
            segment.get(i).setCenterX(segment.get(i-1).getCenterX());
            segment.get(i).setCenterY(segment.get(i-1).getCenterY());
        }
        //Right
        if (kierunek==1){
            if (segment.get(0).getCenterX()>szerokoscOkna+r){
                segment.get(0).setCenterX(-r);
            } else {
                segment.get(0).setCenterX(segment.get(0).getCenterX() + (2 * r));
            }

        }
        //Left
        if (kierunek==3){
            if (segment.get(0).getCenterX()<-r){
                segment.get(0).setCenterX(szerokoscOkna+r);
            } else {
                segment.get(0).setCenterX(segment.get(0).getCenterX() - (2 * r));
            }

        }
        //Up
        if (kierunek==0){
            if (segment.get(0).getCenterY()<0-r){
                segment.get(0).setCenterY(wysokoscOkna+r);
            } else {
                segment.get(0).setCenterY(segment.get(0).getCenterY() - (2 * r));
            }

        }
        //Down
        if (kierunek==2){
            if (segment.get(0).getCenterY()>wysokoscOkna+r){
                segment.get(0).setCenterY(-r);
            } else {
                segment.get(0).setCenterY(segment.get(0).getCenterY() + (2 * r));
            }

        }

    }
    public void setDirection(int kierunek){
        this.kierunek=kierunek;
    }

    public boolean czyZreSamSiebie(){
        boolean test = false;
        for (int i=1;i<segment.size();i++){
            if (Math.abs(segment.get(0).getCenterX()-segment.get(i).getCenterX())<r && Math.abs(segment.get(0).getCenterY()-segment.get(i).getCenterY())<r){
                test=true;
            }
        }
        return test;
    }

}


public class Snake extends Application{
    public int szerokoscOkna=600, wysokoscOkna=600;
    public int kierunek=1;
    public long czas;
    public int szybkosc = 150;
    public Label gameOver = new Label("GAME OVER ... !!!");
    Waz waz = new Waz();
    Node zaslona = new Rectangle(0,0,szerokoscOkna,wysokoscOkna);
    boolean zaslonaIsSet = false;
    long zaslonaTime = 0;
    long szybkoscTime = 0;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Scene scena = new Scene(root,szerokoscOkna,wysokoscOkna);
        Jedzenie jedzenie = new Jedzenie(szerokoscOkna,wysokoscOkna);
        root.getChildren().add(waz.getSegment(0));
        //gameOver.setVisible(false);
        gameOver.setStyle("-fx-font-size:30px; -fx-text-fill: red;-fx-font-weight: bold;");
        gameOver.setLayoutX(250);
        gameOver.setLayoutY(250);
        zaslona.setStyle("-fx-background-color: black");
        scena.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
              switch (event.getCode()) {
                  case A:
                      waz.addSegment();
                      root.getChildren().add(waz.getSegment(waz.getDlugosc()-1));
                      break;
                  case RIGHT:
                      if (kierunek!=3) kierunek = 1;
                      break;
                  case LEFT:
                      if (kierunek!=1) kierunek = 3;
                      break;
                  case UP:
                      if (kierunek!=2) kierunek = 0;
                      break;
                  case DOWN:
                      if (kierunek!=0) kierunek = 2;
                      break;
                  case SPACE:
                      waz.move();
                      break;
              }
              waz.setDirection(kierunek);
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (System.currentTimeMillis()-czas>=szybkosc){
                    waz.move();
                    if (waz.czyZreSamSiebie()) {
                        System.out.println("Zre sam siebie");
                        root.getChildren().add(gameOver);
                        this.stop();
                    }
                    if (waz.segment.get(0).getBoundsInParent().intersects(jedzenie.imageView.getBoundsInParent())){
                        root.getChildren().remove(jedzenie.imageView);
                        switch (jedzenie.typ){
                            case 0:
                                waz.addSegment();
                                root.getChildren().add(waz.getSegment(waz.getDlugosc()-1));
                                break;
                            case 1:
                                if (!zaslonaIsSet) {
                                    root.getChildren().add(zaslona);
                                    zaslonaIsSet = true;
                                    zaslonaTime = System.currentTimeMillis();
                                }
                                break;
                            case 2:
                                szybkosc = 50;
                                szybkoscTime=System.currentTimeMillis();
                                break;
                            case 3:
                                waz.addSegment();
                                root.getChildren().add(waz.getSegment(waz.getDlugosc()-1));
                                break;
                            case 4:
                                waz.addSegment();
                                root.getChildren().add(waz.getSegment(waz.getDlugosc()-1));
                                break;
                        }
                        jedzenie.setX(szerokoscOkna);
                        jedzenie.setY(wysokoscOkna);
                        jedzenie.newFood();
                        root.getChildren().add(jedzenie.imageView);
                    }

                    czas=System.currentTimeMillis();
                }
                jedzenie.newLocation(wysokoscOkna-200,szerokoscOkna-200);
                if (zaslonaIsSet){
                    if (System.currentTimeMillis()-zaslonaTime > 5000){
                        root.getChildren().remove(zaslona);
                        zaslonaIsSet=false;
                    }
                }
                if (szybkosc<150){
                    if (System.currentTimeMillis()- szybkoscTime > 10000){
                        szybkosc=150;
                    }
                }
            }
        };

        czas = System.currentTimeMillis();
        root.getChildren().add(jedzenie.imageView);
        timer.start();

        primaryStage.setScene(scena);
        primaryStage.setTitle("Snake");
        primaryStage.show();
    }



    public static void main(String[] args){
        launch(args);
    }
}
