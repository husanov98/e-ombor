package uz.mh.eombor.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.model.ClientInfo;

@Component
public class ClientMapper {
    private final static ModelMapper modelMapper = new ModelMapper();
    public ClientInfo mapDtoToClient(ClientInfoDto dto){return modelMapper.map(dto, ClientInfo.class);}
    public ClientInfoDto mapToClientInfoDto(ClientInfo clientInfo){return modelMapper.map(clientInfo, ClientInfoDto.class);}
}
