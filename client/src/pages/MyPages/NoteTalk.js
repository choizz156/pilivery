import styled from 'styled-components';
import { useLocation } from 'react-router-dom';
import MypageTalk from '../../components/Talk/MyPageTalk';
import Pagination from '../../components/Etc/Pagination';
import { useGet } from '../../hooks/useFetch';

// 작성글 관리 - 토크
function NoteTalk() {
	const { pathname } = useLocation();
	const { isLoading, isError, data, error } = useGet(
		// 'http://ec2-43-201-37-71.ap-northeast-2.compute.amazonaws.com:8080/talks/mypage',
		'http://localhost:3001/talk',
		pathname,
	);
	const talks = data?.data?.data;
	console.log(talks);

	return (
		<>
			{isLoading || isError ? (
				<div>Loading</div>
			) : (
				<ListContainer>
					{talks.map((talk) => (
						<MypageTalk key={talk.talkId} talk={talk} isReply={talk.reply} />
					))}
				</ListContainer>
			)}
			<Pagination total="10" limit="8" />
		</>
	);
}

const ListContainer = styled.main`
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 4px;
	margin: 25px 0 75px 0;
	border-radius: 10px;
	background-color: white;
	box-shadow: 0px 1px 4px rgba(0, 0, 0, 0.05);
	width: 872px;

	& > {
		:last-child {
			border: none;
		}
	}
`;

export default NoteTalk;
