/**
 * Created by hugueswattez on 15/10/2016.
 *
    200 -> OK
    403 -> Interdit
    404 -> Inconnu
    405 -> Méthode non authorisée
 */
public enum ReturnCode {
    OK, FORBIDDEN, NOT_FOUND, UNAUTHORIZED_METHOD;
}
