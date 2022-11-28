/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import Slider from 'react-slick';
import styled from 'styled-components';
import './slick.css';
import './slick-theme.css';
import { useState } from 'react';
import { MdPlayArrow, MdPause } from 'react-icons/md';
import { IoIosArrowBack } from 'react-icons/io';
import CaroucelInfo from './Caroucel-Info';

const Info = {
	type: 'EVENT',
	title: ['내 몸 구석구석 건강 관리', '하루가 더 가뿐해져요'],
	description: ['멀티비타민 할인전 이벤트', 'NOW ON SALE'],
	color: '#FE9701',
};

const ArrowButton = styled.button`
	width: 40px;
	height: 40px;
	background-color: black;
	border-radius: 50px;
	transition: 0.25s ease;
	& > svg {
		path {
			color: white;
		}
		width: 20px;
		height: 20px;
		position: absolute;
		top: 25%;
		left: 23%;
		${({ right }) =>
			// 180도 회전
			right &&
			`
		transform: rotate(180deg);
		top: 25%;
		left: 28%;
	`}
	}
`;

function MainCaroucel() {
	const [sliderRef, setSliderRef] = useState(null);
	const [isPlaying, setIsPlaying] = useState(true);

	const settings = {
		dots: true,
		infinite: true,
		speed: 500,
		slidesToShow: 2,
		slidesToScroll: 1,
		autoplay: true,
		autoplaySpeed: 5000,
		pauseOnHover: true,
		// variableWidth: true,
		nextArrow: (
			<ArrowButton right>
				<IoIosArrowBack />
			</ArrowButton>
		),
		prevArrow: (
			<ArrowButton left>
				<IoIosArrowBack />
			</ArrowButton>
		),
		className: 'center',
		centerMode: true,
		centerPadding: '0px',
	};
	const pauseFn = () => {
		sliderRef.slickPause();
	};
	const playFn = () => {
		sliderRef.slickPlay();
	};

	return (
		<SlideContainer>
			<Slider {...settings} ref={setSliderRef}>
				{[1, 2, 3, 4, 5, 6].map((el, i) => (
					<SlidePage key={`${i.toString()}-${el}`}>
						<MultiItem>
							<SInfo>
								<CaroucelInfo {...Info} />
							</SInfo>
							<SImg />
						</MultiItem>
					</SlidePage>
				))}
			</Slider>
			<SbuttonBox>
				<Sbutton
					className={isPlaying ? 'hide' : ''}
					onClick={() => {
						playFn();
						setIsPlaying(true);
					}}
				>
					<MdPlayArrow />
				</Sbutton>
				<Sbutton
					className={!isPlaying ? 'hide' : ''}
					onClick={() => {
						pauseFn();
						setIsPlaying(false);
					}}
				>
					<MdPause />
				</Sbutton>
			</SbuttonBox>
		</SlideContainer>
	);
}

export default MainCaroucel;

// * 캐러셀 스타일
export const MultiItem = styled.div`
	opacity: 1;
	display: flex;
`;

export const SImg = styled.img.attrs({
	src: 'https://cdn.discordapp.com/attachments/997446041811046432/1046621249238999140/pills.png',
})`
	width: 55%;
	border-radius: 50%;
`;

const SInfo = styled.div`
	width: 45%;
	padding-left: 180px;
`;

export const SlideContainer = styled.div`
	/* width 옵션으로 전체 width 값을 지정할 수 있음 */
	width: 1090px;

	.slick-center ${MultiItem} {
		/* center 모드일때 center에게 강조할 경우 사용 */
		opacity: 1;
		transition: all 300ms ease;
		transform: scale(1);
		top: 0px;
	}

	${MultiItem} {
		/* center 모드일때 center 외 속성에게 사용 */
		transition: all 300ms ease;
		transform: scale(0.4);
		top: 155px;
	}
`;

export const SlidePage = styled.div`
	${MultiItem} {
		/* center 옵션의 경우 MultiTem 속성을 추가로 사용해서 내부 옵션을 추가로 줘야함 */
		margin: 10px;
		padding: 2%;
		position: relative;
		width: 950px;
		/* 센터아이템 왼쪽에 보여주기 */
		left: -130%;
		.slick-center & {
			/* left: -108%; */
			${SInfo} {
				color: var(--gray-500);
				transition: all 300ms ease;
				opacity: 1;
			}
		}
		${SInfo} {
			transition: all 300ms ease;
			opacity: 0;
		}
	}
`;

// * 버튼 스타일
const SbuttonBox = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	margin: 5px 210px 0 0;
`;

const Sbutton = styled.button`
	border: none;
	background-color: transparent;
	cursor: pointer;
	&.hide {
		display: none;
	}
	z-index: 999;
`;
