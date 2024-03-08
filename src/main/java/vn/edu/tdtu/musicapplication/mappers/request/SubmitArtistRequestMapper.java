package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.SubmitArtistRequest;
import vn.edu.tdtu.musicapplication.enums.EArtistRequestStatus;
import vn.edu.tdtu.musicapplication.enums.EDocumentType;
import vn.edu.tdtu.musicapplication.enums.ESocialNetwork;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.artist_request.*;
import vn.edu.tdtu.musicapplication.service.GenreService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubmitArtistRequestMapper implements Mapper<ArtistRequest, SubmitArtistRequest> {
    private final GenreService genreService;
    @Override
    public ArtistRequest mapToObject(SubmitArtistRequest dto) {
        ArtistRequest request = new ArtistRequest();
        ArtistInfo artistInfo = new ArtistInfo();
        PersonalInfo personalInfo = new PersonalInfo();

        //Personal settings
        personalInfo.setNation(dto.getPersonalInfo().getNation());
        personalInfo.setBankAccountName(dto.getPersonalInfo().getBankAccountName());
        personalInfo.setFullName(dto.getPersonalInfo().getFullName());
        personalInfo.setBankAccountNumber(dto.getPersonalInfo().getBankAccountNumber());
        personalInfo.setBirthDate(dto.getPersonalInfo().getBirthDate());
        personalInfo.setPhoneNumber(dto.getPersonalInfo().getPhoneNumber());
        personalInfo.setArtistInfo(artistInfo);

        List<SocialNetwork> socialNetworks = dto.getPersonalInfo().getSocialNets()
                .stream().map(item -> {
                    SocialNetwork socialNetwork = new SocialNetwork();
                    socialNetwork.setLink(item.getLink());
                    socialNetwork.setType(ESocialNetwork.valueOf(item.getType().toUpperCase()));
                    socialNetwork.setPersonalInfo(personalInfo);

                    return socialNetwork;
                }).toList();

        personalInfo.setSocialNetworks(socialNetworks);

        List<Genre> genres = genreService.findAllByIds(dto.getGenreIds());

        //Artist settings
        artistInfo.setArtistName(dto.getArtistName());
        artistInfo.setActive(true);
        artistInfo.setPersonalInfo(personalInfo);
        artistInfo.setGenres(genres);

        List<OwnerShip> ownerShips = dto.getOwnerShips().stream().map(
                item -> {
                    OwnerShip ownerShip = new OwnerShip();
                    ownerShip.setArtistRequest(request);
                    ownerShip.setImageUrl(item.getImageUrl());

                    return ownerShip;
                }
        ).toList();

        List<Work> works = dto.getWorks().stream().map(
                item -> {
                    Work work = new Work();
                    work.setArtistRequest(request);
                    work.setName(item.getName());
                    work.setAuthor(item.getAuthor());
                    work.setReleaseDate(item.getReleaseDate());

                    return work;
                }
        ).toList();

        List<LegalDocument> legalDocuments = dto.getLegalDocs().stream().map(
                item -> {
                    LegalDocument legalDocs = new LegalDocument();
                    legalDocs.setArtistRequest(request);
                    legalDocs.setLegalDocId(item.getLegalDocId());
                    legalDocs.setDated(item.getDated());
                    legalDocs.setAddress(item.getAddress());
                    legalDocs.setIssuedBy(item.getIssuedBy());

                    return legalDocs;
                }
        ).toList();

        request.setRequestedDate(LocalDateTime.now());
        request.setStatus(EArtistRequestStatus.PENDING);
        request.setArtistInfo(artistInfo);
        request.setOwnerships(ownerShips);
        request.setWorks(works);
        request.setLegalDocuments(legalDocuments);
        request.setActive(true);

        return request;
    }

    @Override
    public SubmitArtistRequest mapToDto(ArtistRequest object) {
        return null;
    }
}
