package com.peersmarket.marketplace.user.application.facade;

import java.util.Optional;
import com.peersmarket.marketplace.user.application.dto.UserProfileViewDto;

public interface UserProfileFacade {

    /**
     * Récupère une vue complète du profil utilisateur, agrégeant les informations
     * de base, les articles, les avis et les articles sauvegardés.
     *
     * @param userId L'ID de l'utilisateur dont le profil est demandé.
     * @return Un Optional contenant le UserProfileViewDto si l'utilisateur est
     *         trouvé, sinon Optional.empty().
     */
    Optional<UserProfileViewDto> getUserProfile(Long userId);
}
