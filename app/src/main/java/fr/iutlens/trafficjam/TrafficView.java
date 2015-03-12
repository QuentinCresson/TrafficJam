package fr.iutlens.trafficjam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import fr.iutlens.trafficjam.traffic.Car;
import fr.iutlens.trafficjam.traffic.Feu;
import fr.iutlens.trafficjam.traffic.LevelMap;
import fr.iutlens.trafficjam.traffic.Signalisation;
import fr.iutlens.trafficjam.traffic.Track;
import fr.iutlens.trafficjam.traffic.Traffic;
import fr.iutlens.trafficjam.util.CoordSystem;
import fr.iutlens.trafficjam.util.SpriteSheet;

/**
 * Created by dubois on 20/01/15.
 */
public class TrafficView extends View {

    LevelMap map;
    CoordSystem coord;

    SpriteSheet sprite;
    Traffic traffic;

    // Entre 0 et 1, proportion du mouvent déjà effectué.
    private float progress;

    // Transformation permettant le centrage de la vue.
    private Matrix transform, reverse;

    // Rectangle réutilisable (pour éviter les instanciations)
    private RectF tmp;


    // Configuration du mode de dessin
    static PaintFlagsDrawFilter setfil= new PaintFlagsDrawFilter(0,
                Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    private Rect src;


    // Configuration du compteur de voitures
    private int nbVoitures; // crée un champ du nombre de voitures à faire passer

    public int getNbVoitures() { // fonction permettant de récupérer le nombre de voiture restant à faire passer
        return nbVoitures;
    } // get du nombre de voitures

    public void deleteCar() {
        nbVoitures--;
    }


    // 3 constructeurs obligatoires pour une vue. Les 3 appellent init() pour ne pas dupliquer le code.

    public TrafficView(Context context) {
        super(context);
        init();
    }

    public TrafficView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /***
     * Fait progresser la vue d'une image
     */
    void act(){
       progress += 0.1; // les voitures en mouvement avancent de 10% d'une case
        if (progress >=1){ // Si on arrive à la case d'après, on recalcule les prochains mouvements.
            progress = 0;
            traffic.moveAll();
        }
        this.invalidate(); // La vue a changé, on demande le rafraîchissement de l'affichage.
    }

    void init(){

        nbVoitures = 60; // nombre de voiture initiailisé en début de partie
        // TODO : nombre de voitures à définir selon la difficulté du jeu

     /*  Niveau de Quentin


      coord = new CoordSystem(-7,2,4,6);

        int[][] data = {
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {5,5,5,5,5,5,5,3,12,13,0,5,5,5,5,5,5,8,20,20},
                {15,15,15,15,15,15,15,15,18,19,15,15,15,15,15,15,16,4,20,20},
                {14,14,14,14,14,14,14,14,19,18,14,14,14,14,14,12,13,4,20,20},
                {6,6,6,6,6,6,6,2,12,13,1,6,6,6,2,12,13,4,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,7,12,13,4,20,20},
                {5,5,5,5,5,5,5,3,12,13,0,5,5,5,3,12,13,0,5,5},
                {15,15,15,15,15,15,15,15,18,19,15,15,15,15,15,18,19,15,15,15},
                {14,14,14,14,14,14,14,14,19,18,14,14,14,14,14,19,18,14,14,14},
                {6,6,6,6,6,6,6,2,12,13,1,6,6,6,2,12,13,1,6,6},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,7,12,13,4,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,7,12,13,4,20,20},
                {5,5,5,5,5,5,5,3,12,13,0,5,5,5,3,12,13,4,20,20},
                {15,15,15,15,15,15,15,15,18,19,15,15,15,15,15,12,13,4,20,20},
                {14,14,14,14,14,14,14,14,19,18,14,14,14,14,14,14,16,4,20,20},
                {6,6,6,6,6,6,6,2,12,13,1,6,6,6,6,6,6,9,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},
                {20,20,20,20,20,20,20,7,12,13,4,20,20,20,20,20,20,20,20,20},





        };
        map = new LevelMap(data);

        Track[] track = new Track[]{

                new Track("9:-1:000000000000000011111111112222222222222222"),
                new Track("-1:8:111111111100000001111111111222222211111"),
                new Track("-1:8:11111111111111111111"),
                new Track("14:-1:000000000000000000000"),
                new Track("14:-1:00000000011111222222222"),

        Fin du niveau Quentin
        */



                //depart des route horizontal
                // 1er route
           //     new Track("-1:2:1111111111111111"),// tout droit
           //     new Track("-1:2:111222"),// 1ere a droite
            //    new Track("-1:2:1111111222"),// 2eme a droite
           //     new Track("-1:2:11111111111222"), // 3eme a droite
            //    new Track("-1:2:11111111111111222"), //4 eme a droite

                //2eme route



              // new Track("-1:2:111222"),//route de droite tourne a droite
             //   new Track("-1:2:1111111"),//route de droite tout droit
             //   new Track("-1:2:1111000"),// route de droit a gauche

            //    new Track("3:-1:000000"),
             //   new Track("2:6:222333"),
             //   new Track("6:3:333000"), //route de gauche tourne a droite
            //    new Track("6:3:333333"),//route de gauche tout droit
           //     new Track("6:3:33332222"),//route de gauche tourne a gauche
        };

        Feu[] feu = new Feu[]{

                new Feu(map,3,8),
                new Feu(map,8,8),
                new Feu(map,14,8),
                new Feu(map,8,15),


        };

        Signalisation signalisation = new Signalisation(feu);
        traffic = new Traffic(map, track, signalisation, this);

        transform = new Matrix();
        reverse = new Matrix();

        sprite = SpriteSheet.get(this.getContext(), R.drawable.sprite);
        src = new Rect(0,0, sprite.w, sprite.h);
    }

    public void onDraw(Canvas canvas){
        canvas.setDrawFilter(setfil);


        // on sauvegarde la transformation en cours et on applique la transformation désirée
        canvas.save();
        canvas.concat(transform);

        if (sprite == null){ //si les sprites ne sont pas chargé, on ne fait rien.
            return;
        }


    //On parcours la carte
        for(int i = 0; i < map.getWidth(); ++i){
            for(int j = 0; j < map.getHeight(); ++j){
                int code = map.map[i][j];
                if (code != -1)
                    canvas.drawBitmap(sprite.getBitmap(code), src,coord.bounds(i,j,i+1,j+1,tmp),null);
            }
        }

     // On parcours les voitures
        for(Car car : traffic.getCar()){
            float i,j;
            i = car.getX(progress);
            j = car.getY(progress);
            canvas.drawBitmap(sprite.getBitmap((car.getAngle() & 1)+21), src,coord.bounds(i,j,i+1,j+1,tmp),null);
        }


        // On restore la transformation originale.
        canvas.restore();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setZoom(w, h);
    }

    /***
     * Calcul du centrage du contenu de la vue
     * @param w
     * @param h
     */
    private void setZoom(int w, int h) {
        if (w<=0 ||h <=0) return;

        // Dimensions dans lesquelles ont souhaite dessiner
        RectF src = coord.bounds(0,0,map.getWidth(),map.getHeight(),tmp);

        src.top += 30;
        src.bottom -= 30;
        src.right -= 70;
        src.left += 70;

        // Dimensions à notre disposition
        RectF dst = new RectF(0,0,w,h);

        // Calcul de la transformation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);
        transform.invert(reverse);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int stopfeu = event.getAction();
        switch (stopfeu) {
            case MotionEvent.ACTION_DOWN:
                traffic.invertLight();
                break;
        }
        return true;


    }

    public int getTmpstotal() {
        return traffic.getTmpstotal();
    }
}
