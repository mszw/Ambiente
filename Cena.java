package ambiente;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT; //primitivas 3D

public class Cena implements GLEventListener, KeyListener {

    private float angulo = 0;
    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private int tonalizacao = GL2.GL_SMOOTH;
    private boolean liga = true;
    private int modo = GL2.GL_FILL;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        GL2 gl = drawable.getGL().getGL2();
        //habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        gl = drawable.getGL().getGL2();
        glut = new GLUT(); //objeto da biblioteca glut

        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada
        //limpa o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, modo);
        /*
            desenho da cena        
        *
         */
        // criar a cena aqui....
        gl.glRotatef(angulo, 0.0f, 1.0f, 1.0f);

        gl.glColor3f(0.0f, 0.5f, 1.0f);
        parede();

        gl.glRotatef(180, 0, 1, 1);
        gl.glColor3f(0.0f, 0.5f, 1.0f);
        parede();

        if (liga) {
            iluminacaoEspecular();
            ligaLuz();
        }

        gl.glRotatef(45, 0, 0, 1);
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glPushMatrix();
        mesa();
        gl.glTranslatef(45, 0, 0);
        gl.glRotatef(180, 0, 1, 0);
        pernas();

        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-45, 0, 0);
        gl.glRotatef(180, 0, 1, 0);
        pernas();

        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 45, 0);
        gl.glRotatef(180, 0, 1, 0);
        pernas();

        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, -45, 0);
        gl.glRotatef(180, 0, 1, 0);
        pernas();

        gl.glPopMatrix();

        abajur();

        if (liga) {
            desligaluz();
        }

        gl.glFlush();
    }

    private void parede() {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-60.0f, 60.0f, -60.0f);
        gl.glVertex3f(60.0f, 60.0f, -60.0f);
        gl.glVertex3f(60.0f, -60.0f, -60.0f);
        gl.glVertex3f(-60.0f, -60.0f, -60.0f);
        gl.glEnd();
    }

    private void mesa() {
        glut.glutSolidCylinder(50, 5, 4, 4);
    }

    private void pernas() {
        glut.glutSolidCylinder(3, 50, 4, 4);
    }

    private void abajur() {
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        glut.glutSolidCylinder(4, 35, 4, 4);
        gl.glTranslatef(0, 0, 35);
        glut.glutSolidSphere(4, 20, 20);
        gl.glColor3f(1.0f, 0.7f, 0.0f);
        gl.glTranslatef(0, 0, -13);
        glut.glutSolidCone(25, 15, 40, 40);
    }

    public void iluminacaoEspecular() {
        float luzAmbiente[] = {0f, 0.0f, 0f, 0f}; //cor
        float luzEspecular[] = {1.0f, 0.0f, 1.0f, 1.0f}; //cor
        float posicaoLuz[] = {35.0f, 35.0f, 35.0f, 0.0f}; //pontual

        //intensidade da reflexao do material        
        int especMaterial = 128;
        //define a concentracao do brilho
        gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, especMaterial);

        //define a reflectÃ¢ncia do material
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, luzEspecular, 0);

        //define os parÃ¢metros de luz de nÃºmero 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void ligaLuz() {
        // habilita a definiÃ§Ã£o da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da iluminaÃ§Ã£o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de nÃºmero 0
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalizaÃ§Ã£o GOURAUD (default)        
        gl.glShadeModel(tonalizacao);
    }

    public void desligaluz() {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
        //desliga a iluminacao
        gl.glDisable(GL2.GL_LIGHTING);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        gl = drawable.getGL().getGL2();
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-100, 100, -100, 100, -100, 100);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            //........
        }
        switch (e.getKeyChar()) {
            case 'r':
                angulo += 45;
                break;
            case 't':
                tonalizacao = tonalizacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
                break;
            // liga / desliga luz
            case 'l':
                if (liga) {
                    liga = false;
                } else {
                    liga = true;
                }
                System.out.println(liga);
                break;
            case 'w':
                if (modo == (GL2.GL_FILL)) {
                    modo = GL2.GL_LINE;
                } else {
                    modo = GL2.GL_FILL;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
