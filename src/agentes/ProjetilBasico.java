package agentes;

public class ProjetilBasico extends AgenteBase {
    int x, y, type, speed;

    public ProjetilBasico(Time timeAtirador) {
        time = timeAtirador;
    }

    // Varia o tipo da sprite de acordo com quem atirou
    @Override
    public String getNomeSprite() {
        return getClass().getSimpleName() + "_" + time.name();
    }
}
