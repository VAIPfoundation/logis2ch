package com.sdc2ch.prcss.ss.executor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.DlvyState;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.State;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.tms.enums.FactoryType;

public class EventExecuteFactory {

	private List<Executor> executors = new ArrayList<>();
	private IShippingContext context;


	public static class EventExecuteFactoryBuilder {
		private IShippingContext context;

		public static EventExecuteFactoryBuilder builder() {
			return new EventExecuteFactoryBuilder();
		}
		public EventExecuteFactoryBuilder context(IShippingContext context) {
			this.context = context;
			return this;
		}
		public EventExecuteFactory build() {
			return new EventExecuteFactory(context);
		}
	}

	private EventExecuteFactory(IShippingContext context) {
		init();
		this.context = context;
	}

	private void init() {
		executors.add(new FactoryEnterExe());
		executors.add(new LoadingStartExe());
		executors.add(new LoadingEndExe());
		executors.add(new FactoryExitExe());
		executors.add(new CenterEnterExe());
		executors.add(new CenterArriveExe());
		executors.add(new CenterUnLoadingExe());
		executors.add(new CenterDepartExe());
		executors.add(new FactoryTurnEnterExe());
		executors.add(new FactoryRecoverExe());
		executors.add(new ReoprtExe());
	}

	public ShippingStateEvent decideEvent(TemplateVo vo) {

		Executor exe = executors.stream().filter(e -> e.supported(vo.getState(), vo.getEventNm())).findFirst().orElseGet(null);
		if(exe != null) {
			exe.setTemplate(vo);
			return exe.exe();
		}
		return null;
	}
	public TemplateVo decideEvent2(TemplateVo vo) {

		Executor exe = executors.stream().filter(e -> e.supported(vo.getState(), vo.getEventNm())).findFirst().orElseGet(null);
		if(exe != null) {
			exe.setTemplate(vo);
			return exe.exe2();
		}
		return null;
	}

	private interface Executor {
		boolean supported(State state, EventNm event);
		void setTemplate(TemplateVo vo);
		ShippingStateEvent exe();
		TemplateVo exe2();
	}

	class FactoryEnterExe implements Executor{

		EventNm event = EventNm.FT_ENTER;
		State state = DlvyState.ENTER;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public ShippingStateEvent exe() {
			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.filter(e -> FactoryType.convert(e.getStopCd()) != FactoryType.FFFF)
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);
				return event;
			}

			return null;
		}
		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}

		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				




				
				ShippingStateEvent _event = template.getEvents().stream()
						.filter(e -> e.getEventBy() == EventBy.GPS).findFirst()
						.orElse(null);

				if(_event != null) {
					template.setEventDt(_event.getEventDt());
					template.setEventBy(_event.getEventBy());
					template.setDataTy(String.format("%s", _event.getEventBy()));
				}
			}
			return template;
		}

	}
	class LoadingStartExe implements Executor{
		EventNm event = EventNm.LDNG_ST;
		State state = DlvyState.LDNG;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}
		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}

		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);

				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);

				template.setRm(String.format("(TMS예정 접차시간 %s)", template.getPlan().getLdngSt()));
				if(event != null) {
					template.setDataTy(String.format("%s-태그 (상차 Dock # %s)", event.getEventBy(), event.getSetupLc()));
				}
				return event;
			}
			return null;
		}

		@Override
		public TemplateVo exe2() {

			template.setRm(String.format("(TMS예정 접차시간 %s)", template.getPlan().getLdngSt()));
			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s-태그 (상차 Dock # %s)", event.getEventBy(), event.getSetupLc()));
			}
			return template;
		}
	}
	class LoadingEndExe implements Executor{

		EventNm event = EventNm.LDNG_ED;
		State state = DlvyState.LDNG;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}

		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});

				List<ShippingStateEvent> events = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.collect(Collectors.toList());

				template.setRm(String.format("(TMS예정 접차시간 %s)", template.getPlan().getLdngSt()));
				if(events.size() > 1) {

					ShippingStateEvent event = events.get(events.size() - 1);
					if(event != null) {
						template.setDataTy(String.format("%s-태그 (하차 Dock # %s)", event.getEventBy(), event.getSetupLc()));
					}
					return events.get(events.size() - 1);
				}
			}

			return null;
		}

		@Override
		public TemplateVo exe2() {
			template.setRm(String.format("(TMS예정 접차시간 %s)", template.getPlan().getLdngSt()));
			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(template.getEvents().size() - 1);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s-태그 (하차 Dock # %s)", event.getEventBy(), event.getSetupLc().lcName));
			}
			return template;
		}
	}
	class FactoryExitExe implements Executor{

		EventNm event = EventNm.FT_EXIT;
		State state = DlvyState.EXIT;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {

				List<ShippingStateEvent> _events = new ArrayList<>();

				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});

				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);

				if(event != null) {
					template.setRm(String.format("(TMS예정 출발시간 %s)", template.getPlan().getLdngEd()));
					template.setDataTy(String.format("%s", event.getEventBy()));
					template.setEventDt(event.getEventDt());
				}

				return event;
			}

			return null;
		}

		@Override
		public TemplateVo exe2() {
			String _rm = template.getPlan().getLdngEd();
			_rm = _rm == null ? "--:--" : _rm;
			template.setRm(String.format("(TMS예정 출발시간 %s)", _rm));

			if(!template.getEvents().isEmpty()) {

				
				ShippingStateEvent _event = template.getEvents().stream()
						.filter(e -> e.getEventBy() == EventBy.MOBILE_WEB).findFirst()
						.orElse(template.getEvents().get(0));

				template.setEventDt(_event.getEventDt());
				template.setEventBy(_event.getEventBy());
				template.setDataTy(String.format("%s", _event.getEventBy()));
			}
			return template;
		}
	}
	class CenterEnterExe implements Executor{

		EventNm event = EventNm.CC_ENTER;
		State state = DlvyState.DELIVERY;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.filter(e -> e.getStopCd().equals(template.getStopCd()))
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);


				if(event != null) {
					template.setRm(null);
					template.setDataTy(String.format("%s", event.getEventBy()));
				}

				return event;
			}

			return null;
		}

		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s", event.getEventBy()));
			}
			return template;
		}
	}
	class CenterArriveExe implements Executor{

		EventNm event = EventNm.CC_ARRIVE;
		State state = DlvyState.DELIVERY;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.filter(e -> e.getStopCd().equals(template.getStopCd()))
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);


				if(event != null) {
					if(!StringUtils.isEmpty(template.getPlan().getArrivePlanTime())) {
						template.setRm(String.format("(TMS예정 도착시간: %s)", template.getPlan().getArrivePlanTime()));
					}
				}


				return event;

			}

			return null;
		}

		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s", event.getEventBy()));

				if(!StringUtils.isEmpty(template.getPlan().getArrivePlanTime())) {
					template.setRm(String.format("(TMS예정 도착시간: %s)", template.getPlan().getArrivePlanTime()));
				}
			}

			return template;
		}
	}
	class CenterUnLoadingExe implements Executor{

		EventNm event = EventNm.CC_TAKEOVER;
		State state = DlvyState.DELIVERY;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});

				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.filter(e -> e.getStopCd().equals(template.getStopCd()))
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);

				if(event != null) {
					if(!StringUtils.isEmpty(event.getCause())) {
						template.setDataTy(String.format("사유:%s", event.getCause()));
					}else {
						int pallet = event.getPalletQty();
						int square = event.getSquareBoxQty();
						int triangle = event.getTriangleBoxQty();
						int yodelry = event.getYodelryBoxQty();
						int tot = pallet + square + triangle + yodelry;
						template.setRm(String.format("공상자 총 %s박스 사각:%s, 삼각:%s, 요델리:%s, 파레트:%s", tot, square, triangle, yodelry, pallet));
						template.setDataTy(String.format("%s", event.getEventBy()));
					}
				}
				return event;
			}

			return null;
		}

		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				if(!StringUtils.isEmpty(event.getCause())) {
					template.setDataTy(String.format("사유:%s", event.getCause()));
				}else {
					int pallet = event.getPalletQty();
					int square = event.getSquareBoxQty();
					int triangle = event.getTriangleBoxQty();
					int yodelry = event.getYodelryBoxQty();
					int tot = pallet + square + triangle + yodelry;
					template.setRm(String.format("공상자 총 %s박스 사각:%s, 삼각:%s, 요델리:%s, 파레트:%s", tot, square, triangle, yodelry, pallet));
					template.setDataTy(String.format("%s", event.getEventBy()));
				}
				template.setEventDt(event.getEventDt());
			}
			return template;
		}
	}
	class CenterDepartExe implements Executor{

		EventNm event = EventNm.CC_DEPART;
		State state = DlvyState.DELIVERY;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.filter(e -> e.getStopCd().equals(template.getStopCd()))
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);

				return event;
			}

			return null;
		}

		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s", event.getEventBy()));
			}
			return template;
		}
	}

	class FactoryTurnEnterExe implements Executor{

		EventNm event = EventNm.FT_ENTER;
		State state = DlvyState.TURN;

		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public ShippingStateEvent exe() {
			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});


				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)

						.filter(e -> FactoryType.convert(e.getStopCd()) != FactoryType.FFFF)
						.max(Comparator.comparing(ShippingStateEvent::getEventDt))
						.orElse(null);
				return null;
			}

			return null;
		}
		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}

		@Override
		public TemplateVo exe2() {








			if(!template.getEvents().isEmpty()) {
				



				
				ShippingStateEvent _event = template.getEvents().stream()
						.filter(e -> e.getEventBy() == EventBy.MOBILE_WEB).findFirst()
						.orElse(null);

				if(_event != null) {
					template.setEventDt(_event.getEventDt());
					template.setEventBy(_event.getEventBy());
					template.setDataTy(String.format("%s", _event.getEventBy()));
				}
			}
			return template;
		}

	}

	class FactoryRecoverExe implements Executor{

		EventNm event = EventNm.FT_RECOVER;
		State state = DlvyState.TURN;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {


			return null;
		}
		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s", event.getEventBy()));
			}
			return template;
		}
	}
	class ReoprtExe implements Executor{

		EventNm event = EventNm.REPORT;
		State state = DlvyState.ENTER;
		TemplateVo template;
		@Override
		public boolean supported(State state, EventNm event) {
			return this.state.getStateCd().equals(state.getStateCd()) && this.event == event;
		}

		@Override
		public void setTemplate(TemplateVo vo) {
			this.template = vo;
		}
		@Override
		public ShippingStateEvent exe() {

			if(event.actions != null) {
				List<ShippingStateEvent> _events = new ArrayList<>();
				Stream.of(event.actions).forEach(a -> {
					List<ShippingStateEvent> events = context.searchEvents(template.getRouteNo(), a);
					if(events != null)
						_events.addAll(events);
				});
				ShippingStateEvent event = _events.stream()
						.filter(e -> e.getEventDt() != null)
						.sorted(Comparator.nullsLast(Comparator.comparing(ShippingStateEvent::getEventDt)))
						.findFirst().orElse(null);
				return event;
			}

			return null;
		}
		@Override
		public TemplateVo exe2() {

			if(!template.getEvents().isEmpty()) {
				ShippingStateEvent event = template.getEvents().get(0);
				template.setEventDt(event.getEventDt());
				template.setEventBy(event.getEventBy());
				template.setDataTy(String.format("%s", event.getEventBy()));
			}
			return template;
		}
	}

}
