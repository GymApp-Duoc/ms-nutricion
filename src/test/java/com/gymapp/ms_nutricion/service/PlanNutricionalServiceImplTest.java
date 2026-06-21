package com.gymapp.ms_nutricion.service;

import com.gymapp.ms_nutricion.assembler.PlanNutricionalAssembler;
import com.gymapp.ms_nutricion.client.GamificacionClient;
import com.gymapp.ms_nutricion.client.MiembroClient;
import com.gymapp.ms_nutricion.client.NotificacionClient;
import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.exception.BusinessException;
import com.gymapp.ms_nutricion.model.PlanNutricional;
import com.gymapp.ms_nutricion.repository.PlanNutricionalRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanNutricionalServiceImplTest {

    @Mock private PlanNutricionalRepository repository;
    @Mock private MiembroClient miembroClient;
    @Mock private GamificacionClient gamificacionClient;
    @Mock private NotificacionClient notificacionClient;
    @Mock private PlanNutricionalAssembler assembler;

    @InjectMocks private PlanNutricionalServiceImpl service;

    @Test
    void crear_MiembroValido_CreaPlan() {

        PlanNutricionalRequestDTO request = new PlanNutricionalRequestDTO(1L, 2L, LocalDate.now(), "Bajar peso", 2000, "Pollo");
        PlanNutricional entity = new PlanNutricional(null, 1L, 2L, LocalDate.now(), "Bajar peso", 2000, "Pollo", true);
        PlanNutricional guardado = new PlanNutricional(1L, 1L, 2L, LocalDate.now(), "Bajar peso", 2000, "Pollo", true);
        PlanNutricionalResponseDTO response = PlanNutricionalResponseDTO.builder().id(1L).caloriasDiarias(2000).build();

        when(miembroClient.obtenerPorId(1L)).thenReturn(new Object());
        when(assembler.toEntity(request)).thenReturn(entity);
        when(repository.save(any(PlanNutricional.class))).thenReturn(guardado);
        when(assembler.toResponseDTO(guardado)).thenReturn(response);


        PlanNutricionalResponseDTO resultado = service.crear(request);


        assertNotNull(resultado);
        assertEquals(2000, resultado.getCaloriasDiarias());
        verify(gamificacionClient, times(1)).enviarEvento(anyMap());
        verify(notificacionClient, times(1)).enviarNotificacion(anyMap());
    }

    @Test
    void crear_MiembroNoExiste_LanzaException() {

        PlanNutricionalRequestDTO request = new PlanNutricionalRequestDTO(99L, 2L, LocalDate.now(), "Objetivo", 2000, "Menu");
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(miembroClient.obtenerPorId(99L)).thenThrow(notFound);


        BusinessException exception = assertThrows(BusinessException.class, () -> service.crear(request));
        assertEquals("Operación rechazada: El miembro asignado no existe.", exception.getMessage());
        verify(repository, never()).save(any());
    }
}